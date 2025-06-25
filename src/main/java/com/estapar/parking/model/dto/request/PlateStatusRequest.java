package com.estapar.parking.model.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;

public record PlateStatusRequest(
        @JsonProperty("license_plate")
        @NotBlank(message = "License plate is required")
        String licensePlate
) {}