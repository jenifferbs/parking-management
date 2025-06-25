package com.estapar.parking.service;

import com.estapar.parking.exception.NotFoundException;
import com.estapar.parking.model.entity.Spot;
import com.estapar.parking.repository.SpotRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SpotServiceTest {

    @Mock
    SpotRepository spotRepo;
    @InjectMocks
    SpotService spotService;

    @Test
    void getSpotStatus_returnsDataWhenSpotExists() {
        var lat = new BigDecimal("1.23");
        var lng = new BigDecimal("4.56");

        var spot = new Spot();
        spot.setIsOccupied(true);
        spot.setOccupiedSince(LocalDateTime.of(2025,6,24,10,0));

        when(spotRepo.findByCoordinates(lat, lng)).thenReturn(Optional.of(spot));

        var spotStatus = spotService.getSpotStatus(lat, lng);

        assertTrue(spotStatus.occupied());
        assertEquals(spot.getOccupiedSince(), spotStatus.entryTime());
    }

    @Test
    void getSpotStatus_throwsWhenNotFound() {
        when(spotRepo.findByCoordinates(any(), any())).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class,
                () -> spotService.getSpotStatus(BigDecimal.ZERO, BigDecimal.ZERO));
    }
}

