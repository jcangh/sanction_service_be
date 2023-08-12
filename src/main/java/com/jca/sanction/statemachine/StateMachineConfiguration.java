package com.jca.sanction.statemachine;

import com.jca.sanction.enums.SanctionEvent;
import com.jca.sanction.enums.SanctionState;
import com.jca.sanction.exception.EventNotAcceptedException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.config.EnableStateMachineFactory;
import org.springframework.statemachine.config.EnumStateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineConfigurationConfigurer;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;
import org.springframework.statemachine.guard.Guard;
import org.springframework.statemachine.listener.StateMachineListener;
import org.springframework.statemachine.listener.StateMachineListenerAdapter;

import java.util.EnumSet;

@Configuration
@EnableStateMachineFactory
public class StateMachineConfiguration extends EnumStateMachineConfigurerAdapter<SanctionState, SanctionEvent> {

    public static final String MESSAGE_HEADER_REQUIRED_APPROVALS = "requiredApprovals";

    public static final String MESSAGE_HEADER_REQUIRED_REJECTIONS = "requiredRejections";

    public static final String MESSAGE_HEADER_APPROVALS_COUNT = "approvalsCount";

    public static final String MESSAGE_HEADER_REJECTIONS_COUNT = "rejectionsCount";

    public static final int SANCTION_DEFAULT_REQUIRED_APPROVALS = 2;

    public static final int SANCTION_DEFAULT_REQUIRED_REJECTIONS = 1;


    @Override
    public void configure(StateMachineTransitionConfigurer<SanctionState, SanctionEvent> transitions) throws Exception {
        transitions
                .withExternal()
                .source(SanctionState.NEW)
                .event(SanctionEvent.ACTIVATE)
                .target(SanctionState.ACTIVE_PENDING);
        transitions
                .withExternal()
                .guard(checkApprovalsGuard())
                .source(SanctionState.ACTIVE_PENDING)
                .event(SanctionEvent.APPROVE)
                .target(SanctionState.ACTIVE);
    }

    @Override
    public void configure(StateMachineStateConfigurer<SanctionState, SanctionEvent> states) throws Exception {
        states.withStates().initial(SanctionState.NEW).states(EnumSet.allOf(SanctionState.class));
    }

    @Override
    public void configure(StateMachineConfigurationConfigurer<SanctionState, SanctionEvent> config)
            throws Exception {
        config.withConfiguration().autoStartup(false).listener(listener());
    }
    @Bean
    public StateMachineListener<SanctionState, SanctionEvent> listener() {
        return new StateMachineListenerAdapter<>() {
            @Override
            public void stateContext(StateContext<SanctionState, SanctionEvent> stateContext) {
                if (stateContext.getStage() == StateContext.Stage.EVENT_NOT_ACCEPTED) {
                    var exception =
                            new EventNotAcceptedException(
                                    stateContext.getEvent(), stateContext.getSource().getId());
                    var stateMachine = stateContext.getStateMachine();
                    stateMachine.getExtendedState().getVariables().put("error", exception);
                    stateMachine.setStateMachineError(exception);
                }
            }
        };
    }

    @Bean
    public Guard<SanctionState, SanctionEvent> checkApprovalsGuard(){
        return stateContext -> {
            final var requiredApprovals =
            stateContext.getMessageHeader(MESSAGE_HEADER_REQUIRED_APPROVALS) != null
                    ? (int) stateContext.getMessageHeader(MESSAGE_HEADER_REQUIRED_APPROVALS)
                    : SANCTION_DEFAULT_REQUIRED_APPROVALS;
            final var approvalCount =
                    stateContext.getMessageHeader(MESSAGE_HEADER_APPROVALS_COUNT) != null
                            ? (int) stateContext.getMessageHeader(MESSAGE_HEADER_APPROVALS_COUNT)
                            : 0;
            return approvalCount >= requiredApprovals;
        };
    }

    @Bean
    public Guard<SanctionState, SanctionEvent> checkRejectionsGuard() {
        return ctx -> {
            final var requiredRejections =
                    ctx.getMessageHeader(MESSAGE_HEADER_REQUIRED_REJECTIONS) != null
                            ? (int) ctx.getMessageHeader(MESSAGE_HEADER_REQUIRED_REJECTIONS)
                            : SANCTION_DEFAULT_REQUIRED_REJECTIONS;
            final var rejectionsCount =
                    ctx.getMessageHeader(MESSAGE_HEADER_REJECTIONS_COUNT) != null
                            ? (int) ctx.getMessageHeader(MESSAGE_HEADER_REJECTIONS_COUNT)
                            : 0;
            return rejectionsCount >= requiredRejections;
        };
    }

}
