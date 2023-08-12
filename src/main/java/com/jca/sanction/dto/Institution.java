package com.jca.sanction.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.jca.sanction.enums.SanctionInstitution;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class Institution extends BaseDto {
    private String id;
    private String name;
    private SanctionInstitution type;
}
