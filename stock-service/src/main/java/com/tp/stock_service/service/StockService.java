package com.tp.stock_service.service;

import com.tp.stock_service.dto.ReserveStockRequest;
import com.tp.stock_service.dto.StockResponse;
import com.tp.stock_service.model.StockItem;
import jakarta.annotation.PostConstruct;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class StockService {

    private final Map<String, StockItem> stock = new ConcurrentHashMap<>();
    private final Map<String, ReserveStockRequest> reservationsByOrderId = new ConcurrentHashMap<>();

    @PostConstruct
    void seedStock() {
        stock.put("prod-1", new StockItem("prod-1", 100, 0));
        stock.put("prod-2", new StockItem("prod-2", 50, 0));
        stock.put("prod-3", new StockItem("prod-3", 5, 0));
    }

    public StockResponse reserve(ReserveStockRequest request) {
        if (request.quantity() <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Quantity must be positive");
        }
        if (reservationsByOrderId.containsKey(request.orderId())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Stock already reserved for order");
        }

        StockItem item = stock.get(request.productId());
        if (item == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found");
        }
        if (item.available() < request.quantity()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Insufficient stock");
        }

        StockItem updated = new StockItem(
                item.productId(),
                item.available() - request.quantity(),
                item.reserved() + request.quantity()
        );
        stock.put(request.productId(), updated);
        reservationsByOrderId.put(request.orderId(), request);

        return new StockResponse(
                request.orderId(),
                request.productId(),
                request.quantity(),
                updated.available(),
                Instant.now(),
                "stock_OK"
        );
    }

    public StockResponse release(String orderId) {
        ReserveStockRequest reservation = reservationsByOrderId.get(orderId);
        if (reservation == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Reservation not found for order");
        }

        StockItem item = stock.get(reservation.productId());
        StockItem updated = new StockItem(
                item.productId(),
                item.available() + reservation.quantity(),
                item.reserved() - reservation.quantity()
        );
        stock.put(reservation.productId(), updated);
        reservationsByOrderId.remove(orderId);

        return new StockResponse(
                orderId,
                reservation.productId(),
                reservation.quantity(),
                updated.available(),
                Instant.now(),
                "stock_released"
        );
    }
}
