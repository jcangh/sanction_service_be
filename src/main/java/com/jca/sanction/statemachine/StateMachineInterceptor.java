package com.jca.sanction.statemachine;

import com.jca.sanction.dto.SanctionEventAction;
import com.jca.sanction.enums.SanctionEvent;
import com.jca.sanction.enums.SanctionState;
import com.jca.sanction.service.SanctionService;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.state.State;
import org.springframework.statemachine.support.StateMachineInterceptorAdapter;
import org.springframework.statemachine.transition.Transition;

import java.util.List;
import java.util.Optional;

import static com.jca.sanction.util.SanctionConstants.*;

@AllArgsConstructor
@Configuration
public class StateMachineInterceptor extends StateMachineInterceptorAdapter<SanctionState, SanctionEvent> {

  private static final List<SanctionEvent> SANCTION_COMMAND_EVENTS =
      List.of(SanctionEvent.APPROVE, SanctionEvent.INACTIVE, SanctionEvent.REJECT);

    private static final List<SanctionEvent> SANCTION_ACTION_EVENTS =
            List.of(SanctionEvent.ACTIVATE, SanctionEvent.CANCEL, SanctionEvent.UPDATE, SanctionEvent.APPROVE);
    private final SanctionService sanctionService;

    @Override
    public void postStateChange(State<SanctionState, SanctionEvent> state, Message<SanctionEvent> message, Transition<SanctionState, SanctionEvent> transition, StateMachine<SanctionState, SanctionEvent> stateMachine, StateMachine<SanctionState, SanctionEvent> rootStateMachine) {
        var messageHeaders = message.getHeaders();
        String sanctionId = messageHeaders.get(MESSAGE_HEADER_SANCTION_ID, String.class);
        sanctionService.updateSanctionState(sanctionId, state.getId());
    }

  @Override
  public void preStateChange(
      State<SanctionState, SanctionEvent> state,
      Message<SanctionEvent> message,
      Transition<SanctionState, SanctionEvent> transition,
      StateMachine<SanctionState, SanctionEvent> stateMachine,
      StateMachine<SanctionState, SanctionEvent> rootStateMachine) {
    var messageHeaders = message.getHeaders();
    if (SANCTION_ACTION_EVENTS.contains(message.getPayload())) {
      addSanctionAction(messageHeaders, message.getPayload(), state.getId());
    }
  }

  @Override
  public StateContext<SanctionState, SanctionEvent> preTransition(
      StateContext<SanctionState, SanctionEvent> stateContext) {
    var messageHeaders = stateContext.getMessage().getHeaders();
    if (SANCTION_COMMAND_EVENTS.contains(stateContext.getEvent())) {
      addSanctionAction(messageHeaders, stateContext.getEvent(), null);
    }
    return stateContext;
  }

  private void addSanctionAction(
      MessageHeaders messageHeaders, SanctionEvent event, SanctionState newState) {
    String sanctionId = messageHeaders.get(MESSAGE_HEADER_SANCTION_ID, String.class);

    var requiredApprovals =
        Optional.ofNullable(messageHeaders.get(MESSAGE_HEADER_REQUIRED_APPROVALS, Integer.class))
            .orElse(SANCTION_DEFAULT_REQUIRED_APPROVALS);
    var requiredRejections =
        Optional.ofNullable(messageHeaders.get(MESSAGE_HEADER_REQUIRED_REJECTIONS, Integer.class))
            .orElse(SANCTION_DEFAULT_REQUIRED_REJECTIONS);
    var actionNote = messageHeaders.get(MESSAGE_HEADER_NOTE, String.class);
    var actionStateBy = messageHeaders.get(MESSAGE_HEADER_ACTION_STATE_BY, String.class);
    SanctionEventAction action =
        new SanctionEventAction(requiredApprovals, requiredRejections, actionNote, actionStateBy);
    sanctionService.addSanctionAction(sanctionId, action, event, newState);
  }
}
