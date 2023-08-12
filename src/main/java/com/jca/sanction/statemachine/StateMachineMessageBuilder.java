package com.jca.sanction.statemachine;

import com.jca.sanction.data.entity.SanctionEntity;
import com.jca.sanction.dto.SanctionEventAction;
import com.jca.sanction.enums.SanctionEvent;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;

import java.util.UUID;

import static com.jca.sanction.util.SanctionConstants.*;

public class StateMachineMessageBuilder<T> {

    private final MessageBuilder<T> messageBuilder;

    public StateMachineMessageBuilder(T payload) {
        this.messageBuilder = MessageBuilder.withPayload(payload);
    }

    public static StateMachineMessageBuilder<SanctionEvent> withSanctionEvent(SanctionEvent event) {
        return new StateMachineMessageBuilder<>(event);
    }

    public StateMachineMessageBuilder<T> withStandardHeaders(String sanctionId) {
        messageBuilder.setHeader(MESSAGE_HEADER_SANCTION_ID, sanctionId);
        return this;
    }

    public StateMachineMessageBuilder<T> withActionHeaders(SanctionEventAction action) {
        messageBuilder.setHeader(
                MESSAGE_HEADER_REQUIRED_APPROVALS,
                action.requiredApprovals());
        messageBuilder.setHeader(
                MESSAGE_HEADER_REQUIRED_REJECTIONS,
                action.requiredRejections());
        messageBuilder.setHeader(
                MESSAGE_HEADER_NOTE, action.note());
        messageBuilder.setHeader(
                MESSAGE_HEADER_ACTION_STATE_BY, action.actionBy());
        return this;
    }

    public StateMachineMessageBuilder<T> withCommandHeaders(
            Integer approvalsCount, Integer rejectionsCount, String note, String actionBy) {
        messageBuilder.setHeader(
                MESSAGE_HEADER_APPROVALS_COUNT, approvalsCount);
        messageBuilder.setHeader(
                MESSAGE_HEADER_REJECTIONS_COUNT, rejectionsCount);
        messageBuilder.setHeader(MESSAGE_HEADER_NOTE, note);
        messageBuilder.setHeader(MESSAGE_HEADER_ACTION_STATE_BY, actionBy);
        return this;
    }

    public StateMachineMessageBuilder<T> withUpdatedSanction(SanctionEntity updatedSanction) {
        messageBuilder.setHeader(
                MESSAGE_HEADER_UPDATED_SANCTION, updatedSanction);
        return this;
    }

    public Message<T> build() {
        return messageBuilder.build();
    }
}
