package com.example.demo.domain.portone.dto;

public record PortOnePaymentResponse(
        String id,
        String status,
        Amount amount
) {
    public record Amount(
            Long total
    ) {}
}