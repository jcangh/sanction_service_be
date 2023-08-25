package com.jca.sanction.data.dao;

import com.jca.sanction.data.entity.SanctionInstitutionEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SanctionInstitutionDao extends CrudRepository<SanctionInstitutionEntity,String> {

    @Query("SELECT m FROM SanctionInstitutionEntity m WHERE m.name LIKE ?1%")
    Optional<SanctionInstitutionEntity> findByNameLike(String name);
}
