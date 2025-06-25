package com.estapar.parking.model.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.List;

@Entity
@Table(name = "sectors")
@Data
@NoArgsConstructor
public class Sector {

    @Id
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "base_price", nullable = false, precision = 10, scale = 2)
    private BigDecimal basePrice;

    @Column(name = "max_capacity", nullable = false)
    private Integer maxCapacity;

    @Column(name = "open_hour", nullable = false)
    private LocalTime openHour;

    @Column(name = "close_hour", nullable = false)
    private LocalTime closeHour;

    @Column(name = "duration_limit_minutes", nullable = false)
    private Integer durationLimitMinutes;

    @Column(name = "current_capacity", nullable = false)
    private Integer currentCapacity = 0;

    @Column(name = "is_closed", nullable = false)
    private Boolean isClosed = false;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)//mappedBy = "sector",
    private List<Spot> spots;

    public double getOccupancyRate() {
        if (maxCapacity == 0) return 0.0;
        return (double) currentCapacity / maxCapacity;
    }
}