package com.jca.sanction.data.entity;

import com.jca.sanction.enums.SanctionState;
import com.jca.sanction.enums.SanctionType;
import io.hypersistence.utils.hibernate.type.json.JsonType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Type;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "sanction")
public class SanctionEntity {

    @Id
    private String id;

    private String name;


    @Enumerated(EnumType.STRING)
    private SanctionType type;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "origin_id", referencedColumnName = "id")
    private SanctionInstitutionEntity origin;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "destination_id", referencedColumnName = "id")
    private SanctionInstitutionEntity destination;

    @Enumerated(EnumType.STRING)
    private SanctionState state;

    @Type(JsonType.class)
    @Column(columnDefinition = "json")
    private Map<String, Object> additionalValues;

    @OneToMany(
            cascade = CascadeType.ALL,
            fetch = FetchType.EAGER
    )
    @JoinColumn(name = "sanction_id")
    @OrderBy(value = "createdBy desc")
    private List<SanctionActionEntity> actions = new ArrayList<>();

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
