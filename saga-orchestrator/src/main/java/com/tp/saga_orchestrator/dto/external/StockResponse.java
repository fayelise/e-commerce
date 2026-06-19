package com.tp.saga_orchestrator.dto.external;

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
