package com.jca.sanction.service;

import com.jca.sanction.data.entity.SanctionInstitutionEntity;
import com.jca.sanction.dto.Institution;

public interface SanctionInstitutionService {

    SanctionInstitutionEntity getInstitutionByNameOrCreate(Institution institution);
}
