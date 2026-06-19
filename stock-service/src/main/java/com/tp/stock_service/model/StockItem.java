package com.tp.stock_service.model;

public record StockItem(
        String productId,
        int available,
        int reserved
) {
}
