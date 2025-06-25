package com.estapar.parking.model.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record RevenueRequest(
        @NotNull(message = "Date is required") LocalDate date,
        @NotBlank(message = "Sector is required") String sector
) {}