package com.jca.sanction.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.jca.sanction.enums.SanctionState;
import com.jca.sanction.enums.SanctionType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class Sanction extends BaseDto {
    private String name;
    private SanctionType type;
    private Institution origin;
    private Institution destination;
    private SanctionState state;
}
