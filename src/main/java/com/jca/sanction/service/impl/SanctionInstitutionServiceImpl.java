package com.jca.sanction.service.impl;

import com.jca.sanction.data.dao.SanctionInstitutionDao;
import com.jca.sanction.data.entity.SanctionInstitutionEntity;
import com.jca.sanction.dto.Institution;
import com.jca.sanction.service.SanctionInstitutionService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@AllArgsConstructor
@Service
public class SanctionInstitutionServiceImpl implements SanctionInstitutionService {

    private final SanctionInstitutionDao sanctionInstitutionDao;

    @Override
    @Transactional
    public SanctionInstitutionEntity getInstitutionByNameOrCreate(Institution institution) {
        Optional<SanctionInstitutionEntity> optional = sanctionInstitutionDao.findByNameLike(institution.getName());

        if (optional.isPresent()){
            return optional.get();
        }
        SanctionInstitutionEntity entity = new SanctionInstitutionEntity();
        entity.setName(institution.getName());
        entity.setType(institution.getType());
        sanctionInstitutionDao.save(entity);
        return entity;
    }
}
