package com.example.demo.domain.portone.dto.request;

public record PortOneCancelRequest(
        String reason,
        String storeId
) {}
