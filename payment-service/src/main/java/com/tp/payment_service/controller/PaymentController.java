package com.tp.payment_service.controller;

import com.tp.payment_service.dto.DebitRequest;
import com.tp.payment_service.dto.PaymentResponse;
import com.tp.payment_service.service.PaymentService;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/payments")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping("/debit")
    public PaymentResponse debit(@RequestBody DebitRequest request) {
        return paymentService.debit(request);
    }

    @PostMapping("/{orderId}/refund")
    public PaymentResponse refund(@PathVariable String orderId) {
        return paymentService.refund(orderId);
    }
}
