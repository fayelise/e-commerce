package com.tp.saga_orchestrator.service;

import com.tp.saga_orchestrator.client.ServiceClients;
import com.tp.saga_orchestrator.dto.PlaceOrderRequest;
import com.tp.saga_orchestrator.dto.SagaResponse;
import com.tp.saga_orchestrator.dto.SagaStepResult;
import com.tp.saga_orchestrator.dto.external.CreateOrderRequest;
import com.tp.saga_orchestrator.dto.external.DebitRequest;
import com.tp.saga_orchestrator.dto.external.OrderResponse;
import com.tp.saga_orchestrator.dto.external.PaymentResponse;
import com.tp.saga_orchestrator.dto.external.ReserveStockRequest;
import com.tp.saga_orchestrator.dto.external.StockResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientResponseException;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class SagaOrchestratorService {

    private final ServiceClients clients;

    public SagaOrchestratorService(ServiceClients clients) {
        this.clients = clients;
    }

    public SagaResponse placeOrder(PlaceOrderRequest request) {
        String sagaId = UUID.randomUUID().toString();
        List<SagaStepResult> steps = new ArrayList<>();
        List<SagaStepResult> compensations = new ArrayList<>();

        String orderId = null;
        boolean paymentDone = false;
        boolean stockDone = false;
        String failureReason = null;

        try {
            OrderResponse order = clients.createOrder(new CreateOrderRequest(
                    request.productId(),
                    request.quantity(),
                    request.amount()
            ));
            orderId = order.id();
            steps.add(new SagaStepResult("1.creer une commande", order.event(), order));

            PaymentResponse payment = clients.debit(new DebitRequest(orderId, request.amount()));
            paymentDone = true;
            steps.add(new SagaStepResult("2.debiter", payment.event(), payment));

            StockResponse stock = clients.reserveStock(new ReserveStockRequest(
                    orderId,
                    request.productId(),
                    request.quantity()
            ));
            stockDone = true;
            steps.add(new SagaStepResult("3.reserver stock", stock.event(), stock));

            OrderResponse confirmed = clients.confirmOrder(orderId);
            steps.add(new SagaStepResult("4.confirmer commande", confirmed.event(), confirmed));

            return new SagaResponse(
                    sagaId,
                    "SUCCESS",
                    "saga terminee avec succes - Commande confirmee",
                    orderId,
                    steps,
                    compensations
            );
        } catch (RestClientResponseException ex) {
            failureReason = ex.getResponseBodyAsString();
        } catch (RuntimeException ex) {
            failureReason = ex.getMessage();
        }

        runCompensations(orderId, paymentDone, stockDone, compensations);
        return new SagaResponse(
                sagaId,
                "FAILED",
                "Saga echouee - compensations executees" + (failureReason != null ? ": " + failureReason : ""),
                orderId,
                steps,
                compensations
        );
    }

    private void runCompensations(
            String orderId,
            boolean paymentDone,
            boolean stockDone,
            List<SagaStepResult> compensations
    ) {
        if (orderId == null) {
            return;
        }
        if (stockDone) {
            StockResponse released = clients.releaseStock(orderId);
            compensations.add(new SagaStepResult("compensation: liberer stock", released.event(), released));
        }
        if (paymentDone) {
            PaymentResponse refunded = clients.refund(orderId);
            compensations.add(new SagaStepResult("compensation: rembourser", refunded.event(), refunded));
        }
        OrderResponse cancelled = clients.cancelOrder(orderId);
        compensations.add(new SagaStepResult("compensation: annuler commande", cancelled.event(), cancelled));
    }
}
