package com.jca.sanction.statemachine.impl;

import com.jca.sanction.data.entity.SanctionEntity;
import com.jca.sanction.dto.Sanction;
import com.jca.sanction.dto.SanctionEventAction;
import com.jca.sanction.enums.SanctionEvent;
import com.jca.sanction.enums.SanctionState;
import com.jca.sanction.service.SanctionService;
import com.jca.sanction.statemachine.StateMachineEventService;
import com.jca.sanction.statemachine.StateMachineInterceptor;
import com.jca.sanction.statemachine.StateMachineMessageBuilder;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.config.StateMachineFactory;
import org.springframework.statemachine.support.DefaultStateMachineContext;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Slf4j
@AllArgsConstructor
@Service
public class StateMachineEventServiceImpl implements StateMachineEventService {

    private final StateMachineFactory<SanctionState, SanctionEvent> stateMachineFactory;

    private final StateMachineInterceptor stateChangeInterceptor;

    private final SanctionService service;

    @Override
    public void activateSanctionEvent(String id, SanctionEventAction action) {
        SanctionEntity sanction = service.getById(id);
        var message = StateMachineMessageBuilder.withSanctionEvent(SanctionEvent.ACTIVATE)
                .withStandardHeaders(sanction.getId())
                .withActionHeaders(action).build();
        sendEventToStateMachine(message, sanction.getState());
    }

    @Override
    public void approveSanctionEvent(String sanctionId, SanctionEventAction action, Integer approvalsCount) {
        SanctionEntity sanction = service.getById(sanctionId);
        var message = StateMachineMessageBuilder.withSanctionEvent(SanctionEvent.APPROVE)
                .withStandardHeaders(sanction.getId())
                .withActionHeaders(action)
                .withCommandHeaders(approvalsCount, 0, action.note(), action.actionBy()).build();
        sendEventToStateMachine(message, sanction.getState());
    }

    @Override
    public void rejectSanctionEvent(String sanctionId, SanctionEventAction action, Integer rejectionsCount) {
        SanctionEntity sanction = service.getById(sanctionId);
        var message = StateMachineMessageBuilder.withSanctionEvent(SanctionEvent.REJECT)
                .withStandardHeaders(sanction.getId())
                .withActionHeaders(action)
                .withCommandHeaders(0, rejectionsCount, action.note(), action.actionBy()).build();
        sendEventToStateMachine(message, sanction.getState());
    }

    @SneakyThrows
    public void sendEventToStateMachine(Message<SanctionEvent> message, SanctionState sanctionState) {
        final var sm = buildSmWithState(sanctionState);
        sm.sendEvent(Mono.just(message)).blockLast();
        if (sm.hasStateMachineError()) {
            throw sm.getExtendedState().get("error", Exception.class);
        }
    }

    private StateMachine<SanctionState, SanctionEvent> buildSmWithState(SanctionState state) {
        final StateMachine<SanctionState, SanctionEvent> stateMachine = stateMachineFactory.getStateMachine();
        stateMachine.stopReactively().block();
        stateMachine
                .getStateMachineAccessor()
                .doWithAllRegions(
                        sma -> {
                            sma.addStateMachineInterceptor(stateChangeInterceptor);
                            sma.resetStateMachineReactively(
                                            new DefaultStateMachineContext<>(state, null, null, null))
                                    .block();
                        });
        stateMachine.startReactively().block();
        return stateMachine;
    }
}
