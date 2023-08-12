package com.jca.sanction.dto;


import com.jca.sanction.enums.SanctionState;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SanctionAction extends BaseDto{

    @Enumerated(EnumType.STRING)
    private SanctionState previousState;

    @Enumerated(EnumType.STRING)
    private SanctionState newState;

    private String approvedBy;
}
