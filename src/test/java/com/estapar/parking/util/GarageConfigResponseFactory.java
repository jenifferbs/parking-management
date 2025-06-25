package com.estapar.parking.util;

import com.estapar.parking.external.dto.GarageConfigResponse;
import com.estapar.parking.external.dto.SectorConfig;
import com.estapar.parking.external.dto.SpotConfig;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.List;

public final class GarageConfigResponseFactory {
    private GarageConfigResponseFactory() {} // utilitário

    /** Retorna um objeto completo e coerente para usar nos testes. */
    public static GarageConfigResponse example() {

        // --- setores ---
        SectorConfig sectorA = new SectorConfig("A", BigDecimal.TEN, 10,
                LocalTime.of(0, 0, 0), LocalTime.of(23, 59, 59), 120);
        SectorConfig sectorB = new SectorConfig("B", BigDecimal.ONE, 2,
                LocalTime.of(0, 0, 0), LocalTime.of(23, 59, 59), 240);

        // --- vagas ---
        SpotConfig a1 = new SpotConfig(1L, "A",
                new BigDecimal("10.0001"), new BigDecimal("20.0001"));
        SpotConfig a2 = new SpotConfig(2L, "A",
                new BigDecimal("10.0002"), new BigDecimal("20.0002"));
        SpotConfig b1 = new SpotConfig(3L, "B",
                new BigDecimal("11.0001"), new BigDecimal("21.0001"));
        SpotConfig b2 = new SpotConfig(4L, "B",
                new BigDecimal("11.0002"), new BigDecimal("21.0002"));

        return new GarageConfigResponse(
                List.of(sectorA, sectorB),
                List.of(a1, a2, b1, b2)
        );
    }
}
