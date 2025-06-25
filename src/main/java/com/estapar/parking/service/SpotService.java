package com.estapar.parking.service;

import com.estapar.parking.exception.NotFoundException;
import com.estapar.parking.model.dto.response.SpotStatusResponse;
import com.estapar.parking.repository.SpotRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class SpotService {

    private final SpotRepository spotRepository;

    public SpotStatusResponse getSpotStatus(BigDecimal lat, BigDecimal lng) {
        var spot = spotRepository.findByCoordinates(lat, lng)
                .orElseThrow(() -> new NotFoundException("Parking spot not found at coordinates: " + lat + ", " + lng));

        return SpotStatusResponse.builder()
                .occupied(spot.getIsOccupied())
                .entryTime(spot.getOccupiedSince())
                .timeParked(spot.getOccupiedSince())
                .build();
    }
}