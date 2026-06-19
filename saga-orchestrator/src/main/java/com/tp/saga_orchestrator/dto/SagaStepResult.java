package com.tp.saga_orchestrator.dto;

import java.util.List;

public record SagaStepResult(
        String step,
        String event,
        Object payload
) {
}
