package com.tp.saga_orchestrator.controller;

import com.tp.saga_orchestrator.dto.PlaceOrderRequest;
import com.tp.saga_orchestrator.dto.SagaResponse;
import com.tp.saga_orchestrator.service.SagaOrchestratorService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OrderSagaController {

    private final SagaOrchestratorService sagaOrchestratorService;

    public OrderSagaController(SagaOrchestratorService sagaOrchestratorService) {
        this.sagaOrchestratorService = sagaOrchestratorService;
    }

    @PostMapping("/order")
    public SagaResponse placeOrder(@RequestBody PlaceOrderRequest request) {
        return sagaOrchestratorService.placeOrder(request);
    }
}
