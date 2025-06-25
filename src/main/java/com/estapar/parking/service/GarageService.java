package com.estapar.parking.service;

import com.estapar.parking.external.GarageSimulatorClient;
import com.estapar.parking.model.entity.Sector;
import com.estapar.parking.model.entity.Spot;
import com.estapar.parking.repository.SectorRepository;
import com.estapar.parking.repository.SpotRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class GarageService {

    private final GarageSimulatorClient garageSimulatorClient;
    private final SectorRepository sectorRepository;
    private final SpotRepository spotRepository;

    @EventListener(ApplicationReadyEvent.class)
    public void initializeGarageConfiguration() {
        log.info("Initializing garage configuration...");

        try {
            var config = garageSimulatorClient.getGarageConfiguration();

            // Clean up existing data
            spotRepository.deleteAll();
            sectorRepository.deleteAll();

            // Save sectors
            config.garage().forEach(sectorConfig -> {
                Sector sector = new Sector();
                sector.setName(sectorConfig.sector());
                sector.setBasePrice(sectorConfig.basePrice());
                sector.setMaxCapacity(sectorConfig.maxCapacity());
                sector.setOpenHour(sectorConfig.openHour());
                sector.setCloseHour(sectorConfig.closeHour());
                sector.setDurationLimitMinutes(sectorConfig.durationLimitMinutes());
                sector.setCurrentCapacity(0);
                sector.setIsClosed(false);

                sectorRepository.save(sector);
                log.info("Saved sector: {}", sector.getName());
            });

            // Save spots
            config.spots().forEach(spotConfig -> {
                Spot spot = new Spot();
                spot.setId(spotConfig.id());
                spot.setSectorName(spotConfig.sector());
                spot.setLatitude(spotConfig.lat());
                spot.setLongitude(spotConfig.lng());
                spot.setIsOccupied(false);

                spotRepository.save(spot);
            });

            log.info("Garage configuration initialized successfully. Sectors: {}, Spots: {}",
                    config.garage().size(), config.spots().size());

        } catch (Exception e) {
            log.error("Failed to initialize garage configuration", e);
        }
    }
}