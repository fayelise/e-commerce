package com.tp.stock_service.dto;

public record ReserveStockRequest(
        String orderId,
        String productId,
        int quantity
) {
}
