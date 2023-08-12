package com.jca.sanction.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.jca.sanction.enums.SanctionInstitution;
import lombok.Builder;
import lombok.Data;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@Builder
public class Institution extends BaseDto {
    private String id;
    private String name;
    private SanctionInstitution type;
}
