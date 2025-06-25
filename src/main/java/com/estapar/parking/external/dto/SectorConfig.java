package com.estapar.parking.external.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.time.LocalTime;

public record SectorConfig(
        @JsonProperty("sector") String sector,
        @JsonProperty("base_price") BigDecimal basePrice,
        @JsonProperty("max_capacity") Integer maxCapacity,
        @JsonProperty("open_hour") LocalTime openHour,
        @JsonProperty("close_hour") LocalTime closeHour,
        @JsonProperty("duration_limit_minutes") Integer durationLimitMinutes
) {}