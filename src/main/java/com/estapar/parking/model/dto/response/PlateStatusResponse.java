package com.estapar.parking.model.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Builder
public record PlateStatusResponse(
        @JsonProperty("license_plate") String licensePlate,
        @JsonProperty("price_until_now") BigDecimal priceUntilNow,
        @JsonProperty("entry_time") @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime entryTime,
        @JsonProperty("time_parked") @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime timeParked,
        @JsonProperty("lat") BigDecimal lat,
        @JsonProperty("lng") BigDecimal lng
) {}