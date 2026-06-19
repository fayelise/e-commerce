package com.tp.saga_orchestrator.dto.external;

import java.math.BigDecimal;

public record DebitRequest(
        String orderId,
        BigDecimal amount
) {
}
