package com.estapar.parking.external.dto;

import java.util.List;

public record GarageConfigResponse (
    List<SectorConfig> garage,
    List<SpotConfig> spots
) {}