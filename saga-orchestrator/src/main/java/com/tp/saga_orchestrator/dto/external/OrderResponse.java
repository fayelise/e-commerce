package com.tp.saga_orchestrator.dto.external;

import java.math.BigDecimal;
import java.time.Instant;

public record OrderResponse(
        String id,
        String productId,
        int quantity,
        BigDecimal amount,
        String status,
        Instant createdAt,
        String event
) {
}
