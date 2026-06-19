package com.tp.saga_orchestrator.dto;

import java.math.BigDecimal;

public record PlaceOrderRequest(
        String productId,
        int quantity,
        BigDecimal amount
) {
}
