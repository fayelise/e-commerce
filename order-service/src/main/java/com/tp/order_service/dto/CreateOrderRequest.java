package com.tp.order_service.dto;

import java.math.BigDecimal;

public record CreateOrderRequest(
        String productId,
        int quantity,
        BigDecimal amount
) {
}
