package com.jca.sanction.service.impl;

import com.jca.sanction.data.dao.SanctionDao;
import com.jca.sanction.data.entity.SanctionActionEntity;
import com.jca.sanction.data.entity.SanctionEntity;
import com.jca.sanction.data.entity.SanctionInstitutionEntity;
import com.jca.sanction.dto.Sanction;
import com.jca.sanction.dto.SanctionEventAction;
import com.jca.sanction.enums.SanctionEvent;
import com.jca.sanction.enums.SanctionState;
import com.jca.sanction.exception.ResourceNotFound;
import com.jca.sanction.service.SanctionInstitutionService;
import com.jca.sanction.service.SanctionService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j
@AllArgsConstructor
@Service
public class SanctionServiceImpl implements SanctionService {

    private final SanctionInstitutionService sanctionInstitutionService;
    private final SanctionDao sanctionDao;


    @Override
    @Transactional
    public Sanction create(Sanction sanction) {

        SanctionInstitutionEntity origin = sanctionInstitutionService.getInstitutionByNameOrCreate(sanction.getOrigin());
        SanctionInstitutionEntity destination = sanctionInstitutionService.getInstitutionByNameOrCreate(sanction.getDestination());

        SanctionEntity entity = new SanctionEntity();
        entity.setName(sanction.getName());
        entity.setType(sanction.getType());
        entity.setState(SanctionState.NEW);

        entity.setOrigin(origin);
        entity.setDestination(destination);
        sanctionDao.save(entity);

        sanction.setId(entity.getId());
        sanction.setCreatedOn(entity.getCreatedOn());
        sanction.setState(entity.getState());
        return sanction;
    }

    @Override
    public SanctionEntity getById(String id) {
        Optional<SanctionEntity> optionalEntity = sanctionDao.findById(id);
        if (optionalEntity.isPresent()){
            return optionalEntity.get();
        }
        throw new ResourceNotFound("Sanction", id);
    }

    @Override
    @Transactional
    public void updateSanctionState(String sanctionId, SanctionState state) {
        Optional<SanctionEntity> optionalEntity = sanctionDao.findById(sanctionId);
        if (optionalEntity.isPresent()){
            SanctionEntity entity = optionalEntity.get();
            entity.setState(state);
            sanctionDao.save(entity);
        }
    }

    @Override
    @Transactional
    public void addSanctionAction(String sanctionId, SanctionEventAction action, SanctionEvent event, SanctionState newState) {
        Optional<SanctionEntity> optionalEntity = sanctionDao.findById(sanctionId);
        if (optionalEntity.isPresent()){
            SanctionEntity entity = optionalEntity.get();

            entity.getAdditionalValues().put("requiredApprovals", action.requiredApprovals());
            entity.getAdditionalValues().put("requiredRejections", action.requiredRejections());

            SanctionActionEntity actionEntity = new SanctionActionEntity();
            actionEntity.setPreviousState(entity.getState());
            actionEntity.setNewState(newState);
            actionEntity.setEvent(event);
            actionEntity.setNote(action.note());
            actionEntity.setApprovedBy(action.actionBy());

            entity.getActions().add(actionEntity);
            sanctionDao.save(entity);
        }
    }
}
