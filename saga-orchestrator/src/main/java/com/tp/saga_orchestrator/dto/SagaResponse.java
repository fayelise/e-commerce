package com.tp.saga_orchestrator.dto;

import java.util.List;

public record SagaResponse(
        String sagaId,
        String status,
        String message,
        String orderId,
        List<SagaStepResult> steps,
        List<SagaStepResult> compensations
) {
}
