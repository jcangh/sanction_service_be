package com.jca.sanction.mapper;

import com.jca.sanction.data.entity.SanctionActionEntity;
import com.jca.sanction.data.entity.SanctionEntity;
import com.jca.sanction.dto.Sanction;
import com.jca.sanction.dto.SanctionAction;
import org.springframework.stereotype.Component;

@Component
public class SanctionMapper {

    public Sanction toSanction(SanctionEntity entity){
        return Sanction.builder()
                .id(entity.getId())
                .name(entity.getName())
                .type(entity.getType())
                .build();
    }

    public SanctionEntity toSanctionEntity(Sanction sanction){
        SanctionEntity entity = new SanctionEntity();
        entity.setName(sanction.getName());
        entity.setType(sanction.getType());
        entity.setState(sanction.getState());
        return entity;
    }

    public SanctionAction toSanctionAction(SanctionActionEntity sanctionAction){
        return SanctionAction.builder()
                .id(sanctionAction.getId())
                .previousState(sanctionAction.getPreviousState())
                .newState(sanctionAction.getNewState())
                .event(sanctionAction.getEvent())
                .note(sanctionAction.getNote())
                .approvedBy(sanctionAction.getApprovedBy())
                .createdOn(sanctionAction.getCreatedOn())
                .build();
    }
}
