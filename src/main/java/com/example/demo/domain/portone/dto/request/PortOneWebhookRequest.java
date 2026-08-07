package com.example.demo.domain.portone.dto.request;

public record PortOneWebhookRequest(
        String type,
        String timestamp,
        Data data
) {
    public record Data(
            String paymentId
    ) {}
}