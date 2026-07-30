package com.example.demo.domain.payment.dto.request;

import jakarta.validation.constraints.NotNull;

public record CreatePaymentRequest(

        @NotNull
        Long orderId,

        @NotNull
        Long addressId
) {
}