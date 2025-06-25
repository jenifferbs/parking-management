package com.estapar.parking.model.dto.webhook;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record WebhookEvent(
        @JsonProperty("license_plate") String licensePlate,
        @JsonProperty("entry_time") LocalDateTime entryTime,
        @JsonProperty("exit_time") LocalDateTime exitTime,
        @JsonProperty("lat") BigDecimal lat,
        @JsonProperty("lng") BigDecimal lng,
        @JsonProperty("event_type") EventType eventType
) {
    public enum EventType {
        ENTRY, PARKED, EXIT
    }
}
