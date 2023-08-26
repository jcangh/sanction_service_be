package com.jca.sanction.service;

import com.jca.sanction.data.entity.SanctionEntity;
import com.jca.sanction.dto.Sanction;
import com.jca.sanction.dto.SanctionEventAction;
import com.jca.sanction.enums.SanctionEvent;
import com.jca.sanction.enums.SanctionState;

import java.util.Map;

public interface SanctionService {

    Sanction create(Sanction sanction);

    SanctionEntity getById(String id);

    void updateSanctionState(String sanctionId, SanctionState state);

    void addSanctionAction(String sanctionId, SanctionEventAction action, SanctionEvent event, SanctionState newState);

    Map<String, Object> getAdditionalValues(String sanctionId);

    Integer getEventCount(SanctionEvent sanctionEvent);
}
