package com.tp.saga_orchestrator.dto.external;

import java.math.BigDecimal;

public record CreateOrderRequest(
        String productId,
        int quantity,
        BigDecimal amount
) {
}
