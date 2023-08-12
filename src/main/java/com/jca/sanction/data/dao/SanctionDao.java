package com.jca.sanction.data.dao;

import com.jca.sanction.data.entity.SanctionEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SanctionDao extends CrudRepository<SanctionEntity,String> {}
