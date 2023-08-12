package com.jca.sanction.service.impl;

import com.jca.sanction.data.dao.SanctionDao;
import com.jca.sanction.data.entity.SanctionEntity;
import com.jca.sanction.data.entity.SanctionInstitutionEntity;
import com.jca.sanction.dto.Sanction;
import com.jca.sanction.enums.SanctionState;
import com.jca.sanction.service.SanctionInstitutionService;
import com.jca.sanction.service.SanctionService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
}
