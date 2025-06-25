package com.estapar.parking.model.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record SpotStatusResponse(
        @JsonProperty("occupied") Boolean occupied,
        @JsonProperty("entry_time") @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime entryTime,
        @JsonProperty("time_parked") @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime timeParked
) {}