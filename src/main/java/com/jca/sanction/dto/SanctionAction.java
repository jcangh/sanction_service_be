package com.jca.sanction.dto;


import com.jca.sanction.enums.SanctionEvent;
import com.jca.sanction.enums.SanctionState;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class SanctionAction extends BaseDto {

    private SanctionState previousState;
    private SanctionState newState;
    private SanctionEvent event;
    private String note;
    private String approvedBy;
}
