package com.tp.stock_service.dto;

import java.time.Instant;

public record StockResponse(
        String orderId,
        String productId,
        int quantity,
        int available,
        Instant processedAt,
        String event
) {
}
