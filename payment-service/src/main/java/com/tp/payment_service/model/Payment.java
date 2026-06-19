package com.tp.payment_service.model;

import java.math.BigDecimal;
import java.time.Instant;

public record Payment(
        String id,
        String orderId,
        BigDecimal amount,
        PaymentStatus status,
        Instant processedAt
) {
}
