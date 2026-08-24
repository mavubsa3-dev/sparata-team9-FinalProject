package com.example.demo.domain.order.dto.request;

import jakarta.validation.constraints.NotNull;

public record SelectOrderAddressRequest(
        @NotNull Long addressId
) {
}