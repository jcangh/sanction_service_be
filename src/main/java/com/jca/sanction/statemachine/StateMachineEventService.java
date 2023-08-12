package com.jca.sanction.statemachine;

import com.jca.sanction.dto.SanctionEventAction;

public interface StateMachineEventService {

    void activateSanctionEvent(String id, SanctionEventAction action);
}
