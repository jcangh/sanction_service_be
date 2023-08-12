package com.jca.sanction.service.impl;

import com.jca.sanction.data.entity.SanctionEntity;
import com.jca.sanction.dto.SanctionAction;
import com.jca.sanction.dto.SanctionEventAction;
import com.jca.sanction.mapper.SanctionMapper;
import com.jca.sanction.service.ActionService;
import com.jca.sanction.service.SanctionService;
import com.jca.sanction.statemachine.StateMachineEventService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

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
}
