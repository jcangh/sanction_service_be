package com.jca.sanction.data.entity;

import com.jca.sanction.enums.SanctionEvent;
import com.jca.sanction.enums.SanctionState;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "sanction_action")
public class SanctionActionEntity {

    @Id
    private String id;

    @Enumerated(EnumType.STRING)
    private SanctionState previousState;

    @Enumerated(EnumType.STRING)
    private SanctionState newState;

    @Enumerated(EnumType.STRING)
    private SanctionEvent event;

    private String note;
    private String approvedBy;

    private Instant createdOn;
    private Instant updatedOn;
    private String createdBy;
    private String updatedBy;

    @PrePersist
    public void beforeCreate() {
        setId(UUID.randomUUID().toString());
        setCreatedOn(Instant.now());
    }

    @PreUpdate
    public void beforeUpdate() {
        setUpdatedOn(Instant.now());
    }
}
