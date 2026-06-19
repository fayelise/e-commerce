package com.tp.order_service.model;

import java.math.BigDecimal;
import java.time.Instant;

public record Order(
        String id,
        String productId,
        int quantity,
        BigDecimal amount,
        OrderStatus status,
        Instant createdAt
) {
}
