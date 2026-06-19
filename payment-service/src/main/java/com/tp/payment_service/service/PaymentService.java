package com.tp.payment_service.service;

import com.tp.payment_service.dto.DebitRequest;
import com.tp.payment_service.dto.PaymentResponse;
import com.tp.payment_service.model.Payment;
import com.tp.payment_service.model.PaymentStatus;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class PaymentService {

    private static final BigDecimal MAX_DEBIT = new BigDecimal("1000.00");

    private final Map<String, Payment> paymentsByOrderId = new ConcurrentHashMap<>();

    public PaymentResponse debit(DebitRequest request) {
        if (request.amount().signum() <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Amount must be positive");
        }
        if (request.amount().compareTo(MAX_DEBIT) > 0) {
            throw new ResponseStatusException(HttpStatus.PAYMENT_REQUIRED, "Insufficient funds");
        }
        if (paymentsByOrderId.containsKey(request.orderId())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Payment already processed for order");
        }

        Payment payment = new Payment(
                UUID.randomUUID().toString(),
                request.orderId(),
                request.amount(),
                PaymentStatus.DEBITED,
                Instant.now()
        );
        paymentsByOrderId.put(request.orderId(), payment);
        return toResponse(payment, "Payment_OK");
    }

    public PaymentResponse refund(String orderId) {
        Payment payment = paymentsByOrderId.get(orderId);
        if (payment == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Payment not found for order");
        }
        if (payment.status() == PaymentStatus.REFUNDED) {
            return toResponse(payment, "Payment_already_refunded");
        }

        Payment refunded = new Payment(
                payment.id(),
                payment.orderId(),
                payment.amount(),
                PaymentStatus.REFUNDED,
                Instant.now()
        );
        paymentsByOrderId.put(orderId, refunded);
        return toResponse(refunded, "Payment_refunded");
    }

    private PaymentResponse toResponse(Payment payment, String event) {
        return new PaymentResponse(
                payment.id(),
                payment.orderId(),
                payment.amount(),
                payment.status(),
                payment.processedAt(),
                event
        );
    }
}
