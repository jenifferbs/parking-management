package com.estapar.parking.service;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
public class PricingService {

    public BigDecimal calculatePrice(BigDecimal basePrice, double occupancyRate) {
        var multiplier = getPriceMultiplier(occupancyRate);
        return basePrice.multiply(multiplier).setScale(2, RoundingMode.HALF_UP);
    }

    public BigDecimal getDiscountPercentage(double occupancyRate) {
        if (occupancyRate < 0.25) {
            return BigDecimal.valueOf(10.0); // 10% discount
        } else if (occupancyRate < 0.50) {
            return BigDecimal.ZERO; // 0% discount
        } else if (occupancyRate < 0.75) {
            return BigDecimal.valueOf(-10.0); // 10% increase
        } else {
            return BigDecimal.valueOf(-25.0); // 25% increase
        }
    }

    private BigDecimal getPriceMultiplier(double occupancyRate) {
        if (occupancyRate < 0.25) {
            return BigDecimal.valueOf(0.90); // 10% discount
        } else if (occupancyRate < 0.50) {
            return BigDecimal.valueOf(1.00); // normal price
        } else if (occupancyRate < 0.75) {
            return BigDecimal.valueOf(1.10); // 10% increase
        } else {
            return BigDecimal.valueOf(1.25); // 25% increase
        }
    }
}