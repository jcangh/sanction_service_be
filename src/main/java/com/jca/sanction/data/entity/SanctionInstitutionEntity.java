package com.jca.sanction.data.entity;

import com.jca.sanction.enums.SanctionInstitution;
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
@Table(name = "sanction_institution")
public class SanctionInstitutionEntity {

    @Id
    private String id;

    private String name;

    @Enumerated(EnumType.STRING)
    private SanctionInstitution type;

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
