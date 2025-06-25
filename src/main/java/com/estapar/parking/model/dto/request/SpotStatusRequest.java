package com.estapar.parking.model.dto.request;

import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record SpotStatusRequest(
        @NotNull(message = "Latitude is required") BigDecimal lat,
        @NotNull(message = "Longitude is required") BigDecimal lng
) {}