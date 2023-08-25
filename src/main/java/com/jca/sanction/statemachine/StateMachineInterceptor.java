package com.jca.sanction.statemachine;

import com.jca.sanction.dto.SanctionEventAction;
import com.jca.sanction.enums.SanctionEvent;
import com.jca.sanction.enums.SanctionState;
import com.jca.sanction.service.SanctionService;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.state.State;
import org.springframework.statemachine.support.StateMachineInterceptorAdapter;
import org.springframework.statemachine.transition.Transition;

import java.util.Optional;

import static com.jca.sanction.util.SanctionConstants.*;

@AllArgsConstructor
@Configuration
public class StateMachineInterceptor extends StateMachineInterceptorAdapter<SanctionState, SanctionEvent> {

    private final SanctionService sanctionService;

    @Override
    public void postStateChange(State<SanctionState, SanctionEvent> state, Message<SanctionEvent> message, Transition<SanctionState, SanctionEvent> transition, StateMachine<SanctionState, SanctionEvent> stateMachine, StateMachine<SanctionState, SanctionEvent> rootStateMachine) {
        var messageHeaders = message.getHeaders();
        String sanctionId = messageHeaders.get(MESSAGE_HEADER_SANCTION_ID, String.class);
        sanctionService.updateSanctionState(sanctionId, state.getId());
    }

    @Override
    public void preStateChange(State<SanctionState, SanctionEvent> state, Message<SanctionEvent> message, Transition<SanctionState, SanctionEvent> transition, StateMachine<SanctionState, SanctionEvent> stateMachine, StateMachine<SanctionState, SanctionEvent> rootStateMachine) {
        var messageHeaders = message.getHeaders();
        String sanctionId = messageHeaders.get(MESSAGE_HEADER_SANCTION_ID, String.class);

        var requiredApprovals =
                Optional.ofNullable(
                                messageHeaders.get(
                                        MESSAGE_HEADER_REQUIRED_APPROVALS, Integer.class))
                        .orElse(SANCTION_DEFAULT_REQUIRED_APPROVALS);
        var requiredRejections =
                Optional.ofNullable(
                                messageHeaders.get(
                                        MESSAGE_HEADER_REQUIRED_REJECTIONS, Integer.class))
                        .orElse(SANCTION_DEFAULT_REQUIRED_REJECTIONS);
        var actionNote =
                messageHeaders.get(MESSAGE_HEADER_NOTE, String.class);
        var actionStateBy =
                messageHeaders.get(MESSAGE_HEADER_ACTION_STATE_BY, String.class);

        SanctionEventAction action = new SanctionEventAction(requiredApprovals, requiredRejections, actionNote, actionStateBy);
        sanctionService.addSanctionAction(sanctionId, action, message.getPayload(), state.getId());
    }

    @Override
    public StateContext<SanctionState, SanctionEvent> preTransition(StateContext<SanctionState, SanctionEvent> stateContext) {
        var messageHeaders = stateContext.getMessage().getHeaders();
        String sanctionId = messageHeaders.get(MESSAGE_HEADER_SANCTION_ID, String.class);

        var requiredApprovals =
                Optional.ofNullable(
                                messageHeaders.get(
                                        MESSAGE_HEADER_REQUIRED_APPROVALS, Integer.class))
                        .orElse(SANCTION_DEFAULT_REQUIRED_APPROVALS);
        var requiredRejections =
                Optional.ofNullable(
                                messageHeaders.get(
                                        MESSAGE_HEADER_REQUIRED_REJECTIONS, Integer.class))
                        .orElse(SANCTION_DEFAULT_REQUIRED_REJECTIONS);
        var actionNote =
                messageHeaders.get(MESSAGE_HEADER_NOTE, String.class);
        var actionStateBy =
                messageHeaders.get(MESSAGE_HEADER_ACTION_STATE_BY, String.class);

        SanctionEventAction action = new SanctionEventAction(requiredApprovals, requiredRejections, actionNote, actionStateBy);
        sanctionService.addSanctionAction(sanctionId, action, stateContext.getEvent(), null);
        return stateContext;
    }
}
