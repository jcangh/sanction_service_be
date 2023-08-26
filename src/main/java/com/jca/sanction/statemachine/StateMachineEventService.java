package com.jca.sanction.statemachine;

import com.jca.sanction.dto.SanctionEventAction;

public interface StateMachineEventService {

    void activateSanctionEvent(String id, SanctionEventAction action);

    void approveSanctionEvent(String sanctionId, SanctionEventAction action, Integer approvalsCount);
    void rejectSanctionEvent(String sanctionId, SanctionEventAction action, Integer rejectionsCount);
}
