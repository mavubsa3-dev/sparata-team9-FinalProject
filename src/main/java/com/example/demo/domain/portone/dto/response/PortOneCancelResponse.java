package com.example.demo.domain.portone.dto.response;

public record PortOneCancelResponse(
        String status,
        Cancellation cancellation
) {
    public record Cancellation(
            String id,
            Long totalAmount,
            String reason
    ) {}
}
