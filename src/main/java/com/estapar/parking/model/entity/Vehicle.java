package com.estapar.parking.model.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "vehicles")
@Data
@NoArgsConstructor
public class Vehicle {
    @Id
    @Column(name = "license_plate", nullable = false)
    private String licensePlate;

    @Column(name = "entry_time", nullable = false)
    private LocalDateTime entryTime;

    @Column(name = "exit_time")
    private LocalDateTime exitTime;

    @Column(name = "parked_time")
    private LocalDateTime parkedTime;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private VehicleStatus status = VehicleStatus.ENTERED;

    @Column(name = "sector_name")
    private String sectorName;

    @Column(name = "spot_id")
    private Long spotId;

    public enum VehicleStatus {
        ENTERED, PARKED, EXITED
    }
}