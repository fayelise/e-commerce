package com.tp.payment_service.dto;

import com.tp.payment_service.model.PaymentStatus;

import java.math.BigDecimal;
import java.time.Instant;

public record PaymentResponse(
        String id,
        String orderId,
        BigDecimal amount,
        PaymentStatus status,
        Instant processedAt,
        String event
) {
}
