package com.tp.saga_orchestrator.client;

import com.tp.saga_orchestrator.dto.external.CreateOrderRequest;
import com.tp.saga_orchestrator.dto.external.DebitRequest;
import com.tp.saga_orchestrator.dto.external.OrderResponse;
import com.tp.saga_orchestrator.dto.external.PaymentResponse;
import com.tp.saga_orchestrator.dto.external.ReserveStockRequest;
import com.tp.saga_orchestrator.dto.external.StockResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class ServiceClients {

    private final RestClient orderClient;
    private final RestClient paymentClient;
    private final RestClient stockClient;

    public ServiceClients(
            RestClient.Builder builder,
            @Value("${services.order.url}") String orderUrl,
            @Value("${services.payment.url}") String paymentUrl,
            @Value("${services.stock.url}") String stockUrl
    ) {
        this.orderClient = builder.baseUrl(orderUrl).build();
        this.paymentClient = builder.baseUrl(paymentUrl).build();
        this.stockClient = builder.baseUrl(stockUrl).build();
    }

    public OrderResponse createOrder(CreateOrderRequest request) {
        return orderClient.post()
                .uri("/orders")
                .body(request)
                .retrieve()
                .body(OrderResponse.class);
    }

    public OrderResponse confirmOrder(String orderId) {
        return orderClient.post()
                .uri("/orders/{orderId}/confirm", orderId)
                .retrieve()
                .body(OrderResponse.class);
    }

    public OrderResponse cancelOrder(String orderId) {
        return orderClient.post()
                .uri("/orders/{orderId}/cancel", orderId)
                .retrieve()
                .body(OrderResponse.class);
    }

    public PaymentResponse debit(DebitRequest request) {
        return paymentClient.post()
                .uri("/payments/debit")
                .body(request)
                .retrieve()
                .body(PaymentResponse.class);
    }

    public PaymentResponse refund(String orderId) {
        return paymentClient.post()
                .uri("/payments/{orderId}/refund", orderId)
                .retrieve()
                .body(PaymentResponse.class);
    }

    public StockResponse reserveStock(ReserveStockRequest request) {
        return stockClient.post()
                .uri("/stock/reserve")
                .body(request)
                .retrieve()
                .body(StockResponse.class);
    }

    public StockResponse releaseStock(String orderId) {
        return stockClient.post()
                .uri("/stock/{orderId}/release", orderId)
                .retrieve()
                .body(StockResponse.class);
    }
}
