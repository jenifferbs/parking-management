package com.estapar.parking.service;

import com.estapar.parking.exception.AlreadyExistsException;
import com.estapar.parking.exception.MaxCapacityReachedException;
import com.estapar.parking.exception.NotFoundException;
import com.estapar.parking.model.dto.response.PlateStatusResponse;
import com.estapar.parking.model.dto.webhook.WebhookEvent;
import com.estapar.parking.model.entity.ParkingSession;
import com.estapar.parking.model.entity.Spot;
import com.estapar.parking.model.entity.Vehicle;
import com.estapar.parking.repository.ParkingSessionRepository;
import com.estapar.parking.repository.SectorRepository;
import com.estapar.parking.repository.SpotRepository;
import com.estapar.parking.repository.VehicleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class VehicleService {

    private final VehicleRepository vehicleRepository;
    private final SpotRepository spotRepository;
    private final ParkingSessionRepository parkingSessionRepository;
    private final SectorRepository sectorRepository;
    private final PricingService pricingService;

    public void handleVehicleEntry(WebhookEvent event) {
        log.info("Processing vehicle entry: {}", event.licensePlate());

        // Only accepts if vehicle didn't entry the garage or entered and already left (status COMPLETED)
        parkingSessionRepository.findByLicensePlateAndStatus(event.licensePlate(), ParkingSession.SessionStatus.ACTIVE)
                .ifPresent(session -> {
                    throw new AlreadyExistsException("Parking session already exists for license plate " + event.licensePlate());
                });

        var vehicle = new Vehicle();
        vehicle.setLicensePlate(event.licensePlate());
        vehicle.setEntryTime(event.entryTime());
        vehicle.setStatus(Vehicle.VehicleStatus.ENTERED);

        vehicleRepository.save(vehicle);

        // Create parking session
        var session = new ParkingSession();
        session.setLicensePlate(event.licensePlate());
        session.setEntryTime(event.entryTime());
        session.setStatus(ParkingSession.SessionStatus.ACTIVE);

        parkingSessionRepository.save(session);
    }

    public void handleVehicleParked(WebhookEvent event) {
        log.info("Processing vehicle parked: {} at {}, {}",
                event.licensePlate(), event.lat(), event.lng());

        // Find spot by coordinates
        var spot = spotRepository.findByCoordinates(event.lat(), event.lng())
                .orElseThrow(() -> new NotFoundException("Parking spot not found at coordinates: " + event.lat() + ", " + event.lng()));

        if (spot.getIsOccupied()) {
            throw new MaxCapacityReachedException("Parking spot at the specified coordinates is already occupied");
        }

        // Find vehicle by license plate
        var vehicle = vehicleRepository.findById(event.licensePlate())
                .orElseThrow(() -> new NotFoundException("Vehicle not found: " + event.licensePlate()));

        // Find sector by spot's sector name
        var sector = sectorRepository.findById(spot.getSectorName())
                .orElseThrow(() -> new NotFoundException("Sector not found: " + spot.getSectorName()));

        // Verify if the sector is fully occupied
        if (Objects.equals(sector.getCurrentCapacity(), sector.getMaxCapacity())) {
            throw new MaxCapacityReachedException("All spots are occupied at sector " + sector.getName());
        }

        // Update vehicle information
        vehicle.setParkedTime(LocalDateTime.now());
        vehicle.setSectorName(spot.getSectorName());
        vehicle.setSpotId(spot.getId());
        vehicle.setStatus(Vehicle.VehicleStatus.PARKED);

        // Update spot information
        spot.setIsOccupied(true);
        spot.setOccupiedSince(LocalDateTime.now());
        spot.setCurrentLicensePlate(event.licensePlate());

        // Update sector information
        sector.setCurrentCapacity(sector.getCurrentCapacity() + 1);
        if (Objects.equals(sector.getCurrentCapacity(), sector.getMaxCapacity())) {
            sector.setIsClosed(true);
        }

        sectorRepository.save(sector);

        // Update parking session with pricing details
        var session = parkingSessionRepository.findByLicensePlateAndStatus(event.licensePlate(),
                        ParkingSession.SessionStatus.ACTIVE)
                .orElseThrow(() -> new NotFoundException("Parking session not found for license plate: " + event.licensePlate()));

        session.setSectorName(spot.getSectorName());
        session.setSpotId(spot.getId());
        session.setParkedTime(LocalDateTime.now());

        var basePrice = sector.getBasePrice();
        var finalPrice = pricingService.calculatePrice(basePrice, sector.getOccupancyRate());
        var discountPercentage = pricingService.getDiscountPercentage(sector.getOccupancyRate());

        session.setBasePrice(basePrice);
        session.setFinalPrice(finalPrice);
        session.setDiscountPercentage(discountPercentage);

        parkingSessionRepository.save(session);
        vehicleRepository.save(vehicle);
        spotRepository.save(spot);
    }

    public void handleVehicleExit(WebhookEvent event) {
        log.info("Processing vehicle exit: {}", event.licensePlate());

        var vehicle = vehicleRepository.findById(event.licensePlate())
                .orElseThrow(() -> new NotFoundException("Vehicle not found: " + event.licensePlate()));

        vehicle.setExitTime(event.exitTime());
        vehicle.setStatus(Vehicle.VehicleStatus.EXITED);

        // Free the parking spot
        if (vehicle.getSpotId() != null) {
            Spot spot = spotRepository.findById(vehicle.getSpotId()).orElse(null);
            if (spot != null) {
                spot.setIsOccupied(false);
                spot.setOccupiedSince(null);
                spot.setCurrentLicensePlate(null);
                spotRepository.save(spot);
            }
        }

        // Update sector capacity
        if (vehicle.getSectorName() != null) {
            var sector = sectorRepository.findById(vehicle.getSectorName())
                    .orElseThrow(() -> new NotFoundException("Sector not found: " + vehicle.getSectorName()));

            sector.setCurrentCapacity(Math.max(0, sector.getCurrentCapacity() - 1));
            // Reopen sector
            sector.setIsClosed(false);
            sectorRepository.save(sector);
        }

        // Finish parking session
        var session = parkingSessionRepository.findByLicensePlateAndStatus(
                        event.licensePlate(), ParkingSession.SessionStatus.ACTIVE)
                .orElseThrow(() -> new NotFoundException("Parking session not found for license plate: " + event.licensePlate()));

        session.setExitTime(event.exitTime());
        session.setStatus(ParkingSession.SessionStatus.COMPLETED);
        parkingSessionRepository.save(session);

        vehicleRepository.save(vehicle);
    }

    public PlateStatusResponse getPlateStatus(String licensePlate) {
        var vehicle = vehicleRepository.findById(licensePlate)
                .orElseThrow(() -> new NotFoundException("Vehicle not found: " + licensePlate));

        var session = parkingSessionRepository.findByLicensePlateAndStatus(
                        licensePlate, ParkingSession.SessionStatus.ACTIVE)
                .orElseThrow(() -> new NotFoundException("Parking session not found for license plate: " + licensePlate));

        var priceUntilNow = BigDecimal.ZERO;
        if (session.getFinalPrice() != null) {
            priceUntilNow = session.getFinalPrice();
        }

        return PlateStatusResponse.builder()
                .licensePlate(licensePlate)
                .priceUntilNow(priceUntilNow)
                .entryTime(vehicle.getEntryTime())
                .timeParked(vehicle.getParkedTime())
                .lat(getVehicleLatitude(vehicle))
                .lng(getVehicleLongitude(vehicle))
                .build();
    }

    private BigDecimal getVehicleLatitude(Vehicle vehicle) {
        if (vehicle.getSpotId() != null) {
            var spot = spotRepository.findById(vehicle.getSpotId())
                    .orElseThrow(() -> new NotFoundException("Spot not found: " + vehicle.getSpotId()));
            return spot.getLatitude();
        }
        return null;
    }

    private BigDecimal getVehicleLongitude(Vehicle vehicle) {
        if (vehicle.getSpotId() != null) {
            var spot = spotRepository.findById(vehicle.getSpotId()).orElseThrow(() -> new NotFoundException("Spot not found: " + vehicle.getSpotId()));
            return spot.getLongitude();
        }
        return null;
    }
}