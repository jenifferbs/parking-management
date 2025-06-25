package com.estapar.parking.service;

import com.estapar.parking.model.dto.response.RevenueResponse;
import com.estapar.parking.repository.ParkingSessionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class RevenueService {

    private final ParkingSessionRepository parkingSessionRepository;

    public RevenueResponse calculateRevenue(LocalDate date, String sector) {
        var startOfDay = date.atStartOfDay();
        var endOfDay = date.atTime(23, 59, 59);

        var totalRevenue = parkingSessionRepository
                .findCompletedSessionsBySectorAndDateRange(sector, startOfDay, endOfDay)
                .stream()
                .map(session -> session.getFinalPrice() != null ? session.getFinalPrice() : BigDecimal.ZERO)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return RevenueResponse.builder()
                .amount(totalRevenue)
                .currency("BRL")
                .timestamp(LocalDateTime.now())
                .build();
    }
}