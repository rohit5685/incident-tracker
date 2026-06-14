package com.comcast.incidenttracker.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class IncidentRequest {

    @NotNull
    private Long serviceId;

    @NotBlank
    private String severity; // P1, P2, P3, P4

    @NotBlank
    private String description;
}
