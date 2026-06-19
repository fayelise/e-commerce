package com.tp.saga_orchestrator.dto.external;

public record ReserveStockRequest(
        String orderId,
        String productId,
        int quantity
) {
}
