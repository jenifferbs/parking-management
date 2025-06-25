package com.estapar.parking.service;

import com.estapar.parking.model.entity.ParkingSession;
import com.estapar.parking.repository.ParkingSessionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RevenueServiceTest {

    @Mock
    ParkingSessionRepository parkingRepo;
    @InjectMocks
    RevenueService revenueService;

    @Test
    void calculateRevenue_sumsFinalPrices() {
        var s1 = new ParkingSession(); s1.setFinalPrice(BigDecimal.TEN);
        var s2 = new ParkingSession(); s2.setFinalPrice(new BigDecimal("5"));

        when(parkingRepo.findCompletedSessionsBySectorAndDateRange(anyString(), any(), any()))
                .thenReturn(List.of(s1, s2));

        var revenue = revenueService.calculateRevenue(LocalDate.now(), "A");

        assertEquals(new BigDecimal("15"), revenue.amount());
        assertEquals("BRL", revenue.currency());
    }
}

