package com.example.demo.domain.payment.dto.request;

import jakarta.validation.constraints.NotBlank;

public record ConfirmPaymentRequest(
        @NotBlank
        String portonePaymentId
) {}