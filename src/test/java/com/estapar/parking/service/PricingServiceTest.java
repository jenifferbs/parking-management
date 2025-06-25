package com.estapar.parking.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PricingServiceTest {

    private final PricingService service = new PricingService();

    @ParameterizedTest
    @CsvSource({
            "100, 0.10,  90.00",   // 10 % desconto
            "100, 0.40, 100.00",   // preço normal
            "100, 0.60, 110.00",   // 10 % aumento
            "100, 0.90, 125.00"    // 25 % aumento
    })
    void calculatePrice_shouldApplyMultiplier(BigDecimal basePrice, double occupancy, BigDecimal expected) {
        var result = service.calculatePrice(basePrice, occupancy);
        assertEquals(expected, result);
    }

    @Test
    void getDiscountPercentage_shouldReturnNegativeForSurcharge() {
        assertEquals(BigDecimal.valueOf(-25.0), service.getDiscountPercentage(0.80));
    }
}
