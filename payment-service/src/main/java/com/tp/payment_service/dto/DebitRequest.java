package com.tp.payment_service.dto;

import java.math.BigDecimal;

public record DebitRequest(
        String orderId,
        BigDecimal amount
) {
}
