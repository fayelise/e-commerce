package com.tp.order_service.dto;

import com.tp.order_service.model.OrderStatus;

import java.math.BigDecimal;
import java.time.Instant;

public record OrderResponse(
        String id,
        String productId,
        int quantity,
        BigDecimal amount,
        OrderStatus status,
        Instant createdAt,
        String event
) {
}
