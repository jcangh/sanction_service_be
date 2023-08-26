package com.jca.sanction.data.dao;

import com.jca.sanction.data.entity.SanctionEntity;
import com.jca.sanction.enums.SanctionEvent;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Map;

@Repository
public interface SanctionDao extends CrudRepository<SanctionEntity,String> {

    @Query("SELECT s.additionalValues FROM SanctionEntity s WHERE s.id = :sanctionId")
    Map<String, Object> getAdditionalValues(String sanctionId);

    @Query("SELECT count(s.id) FROM SanctionEntity s JOIN s.actions a WHERE a.event = :sanctionEvent")
    Integer countSanctionEvents(SanctionEvent sanctionEvent);
}
