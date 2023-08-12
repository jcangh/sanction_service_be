package com.jca.sanction.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Setter
public class BaseDto {
    private String id;
    private Instant createdOn;
    private Instant updatedOn;
    private String createdBy;
    private String updatedBy;
}
