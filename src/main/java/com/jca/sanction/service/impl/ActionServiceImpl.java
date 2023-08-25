package com.jca.sanction.service.impl;

import com.jca.sanction.data.entity.SanctionEntity;
import com.jca.sanction.dto.SanctionAction;
import com.jca.sanction.dto.SanctionEventAction;
import com.jca.sanction.enums.SanctionEvent;
import com.jca.sanction.mapper.SanctionMapper;
import com.jca.sanction.service.ActionService;
import com.jca.sanction.service.SanctionService;
import com.jca.sanction.statemachine.StateMachineEventService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.jca.sanction.util.SanctionConstants.REQUIRED_APPROVALS;
import static com.jca.sanction.util.SanctionConstants.REQUIRED_REJECTIONS;

@Slf4j
@AllArgsConstructor
@Service
public class ActionServiceImpl implements ActionService {

    private final SanctionService sanctionService;
    private final StateMachineEventService eventService;

    private final SanctionMapper sanctionMapper;

    @Override
    public List<SanctionAction> activateSanction(String sanctionId, SanctionEventAction action) {
        eventService.activateSanctionEvent(sanctionId, action);
        SanctionEntity entity = sanctionService.getById(sanctionId);
        return entity.getActions().stream().map(sanctionMapper::toSanctionAction).toList();
    }

    @Override
    public List<SanctionAction> approveSanction(String sanctionId, String note, String approvedBy) {
        var entity = sanctionService.getById(sanctionId);
        var approvalsCount = entity.getActions().stream()
                .filter( action -> SanctionEvent.APPROVE.equals(action.getEvent())).count() + 1;
    eventService.approveSanctionEvent(
        entity.getId(),
        new SanctionEventAction(
            (Integer) entity.getAdditionalValues().get(REQUIRED_APPROVALS),
            (Integer) entity.getAdditionalValues().get(REQUIRED_REJECTIONS),
            note,
            approvedBy),
        (int) approvalsCount);
        return sanctionService.getById(sanctionId).getActions().stream().map(sanctionMapper::toSanctionAction).toList();
    }
}
