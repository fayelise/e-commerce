package com.tp.saga_orchestrator.dto.external;

import java.math.BigDecimal;
import java.time.Instant;

public record PaymentResponse(
        String id,
        String orderId,
        BigDecimal amount,
        String status,
        Instant processedAt,
        String event
) {
}
