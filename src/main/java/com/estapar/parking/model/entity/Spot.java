package com.estapar.parking.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "spots")
@Data
@NoArgsConstructor
public class Spot {

    @Id
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "sector_name", nullable = false)
    private String sectorName;

    @Column(name = "latitude", nullable = false, precision = 9, scale = 6)
    private BigDecimal latitude;

    @Column(name = "longitude", nullable = false, precision = 10, scale = 6)
    private BigDecimal longitude;

    @Column(name = "is_occupied", nullable = false)
    private Boolean isOccupied = false;

    @Column(name = "occupied_since")
    private LocalDateTime occupiedSince;

    @Column(name = "current_license_plate")
    private String currentLicensePlate;
}