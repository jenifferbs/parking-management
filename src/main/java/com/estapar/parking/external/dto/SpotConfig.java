package com.estapar.parking.external.dto;

import java.math.BigDecimal;

public record SpotConfig(
        Long id,
        String sector,
        BigDecimal lat,
        BigDecimal lng
) {}