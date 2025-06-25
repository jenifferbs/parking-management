package com.estapar.parking.service;

import com.estapar.parking.external.GarageSimulatorClient;
import com.estapar.parking.model.entity.Sector;
import com.estapar.parking.model.entity.Spot;
import com.estapar.parking.repository.SectorRepository;
import com.estapar.parking.repository.SpotRepository;
import com.estapar.parking.util.GarageConfigResponseFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GarageServiceTest {

    @Mock
    GarageSimulatorClient garageClient;
    @Mock
    SectorRepository sectorRepo;
    @Mock
    SpotRepository spotRepo;
    @InjectMocks
    GarageService garageService;

    @Test
    void initializeGarageConfiguration_persistsSectorsAndSpots() {
        var cfg = GarageConfigResponseFactory.example();

        when(garageClient.getGarageConfiguration()).thenReturn(cfg);

        garageService.initializeGarageConfiguration();

        verify(sectorRepo, times(2)).save(any(Sector.class));
        verify(spotRepo, times(4)).save(any(Spot.class));
        verify(spotRepo).deleteAll();
    }
}

