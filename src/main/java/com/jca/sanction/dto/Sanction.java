package com.jca.sanction.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.jca.sanction.enums.SanctionState;
import com.jca.sanction.enums.SanctionType;
import lombok.Builder;
import lombok.Data;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@Builder
public class Sanction extends BaseDto {
    private String name;
    private SanctionType type;
    private Institution origin;
    private Institution destination;
    private SanctionState state;
}
