package com.tp.order_service.service;

import com.tp.order_service.dto.CreateOrderRequest;
import com.tp.order_service.dto.OrderResponse;
import com.tp.order_service.model.Order;
import com.tp.order_service.model.OrderStatus;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class OrderService {

    private final Map<String, Order> orders = new ConcurrentHashMap<>();

    public OrderResponse createOrder(CreateOrderRequest request) {
        if (request.quantity() <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Quantity must be positive");
        }
        if (request.amount().signum() <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Amount must be positive");
        }

        String id = UUID.randomUUID().toString();
        Order order = new Order(
                id,
                request.productId(),
                request.quantity(),
                request.amount(),
                OrderStatus.CREATED,
                Instant.now()
        );
        orders.put(id, order);
        return toResponse(order, "order_created");
    }

    public OrderResponse confirmOrder(String orderId) {
        Order order = getOrder(orderId);
        if (order.status() != OrderStatus.CREATED) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Order cannot be confirmed");
        }
        Order confirmed = new Order(
                order.id(),
                order.productId(),
                order.quantity(),
                order.amount(),
                OrderStatus.CONFIRMED,
                order.createdAt()
        );
        orders.put(orderId, confirmed);
        return toResponse(confirmed, "order_confirmed");
    }

    public OrderResponse cancelOrder(String orderId) {
        Order order = getOrder(orderId);
        if (order.status() == OrderStatus.CANCELLED) {
            return toResponse(order, "order_already_cancelled");
        }
        Order cancelled = new Order(
                order.id(),
                order.productId(),
                order.quantity(),
                order.amount(),
                OrderStatus.CANCELLED,
                order.createdAt()
        );
        orders.put(orderId, cancelled);
        return toResponse(cancelled, "order_cancelled");
    }

    public OrderResponse getOrderResponse(String orderId) {
        return toResponse(getOrder(orderId), null);
    }

    private Order getOrder(String orderId) {
        Order order = orders.get(orderId);
        if (order == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Order not found");
        }
        return order;
    }

    private OrderResponse toResponse(Order order, String event) {
        return new OrderResponse(
                order.id(),
                order.productId(),
                order.quantity(),
                order.amount(),
                order.status(),
                order.createdAt(),
                event
        );
    }
}
