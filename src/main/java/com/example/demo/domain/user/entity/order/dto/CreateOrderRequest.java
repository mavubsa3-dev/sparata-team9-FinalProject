package com.example.demo.domain.order.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record CreateOrderRequest(
        @NotEmpty List<Long> cartItemIds,
        @NotNull Long addressId
) {
    public static CreateOrderRequest of(List<Long> cartItemIds, Long addressId) {
        return new CreateOrderRequest(cartItemIds, addressId);
    }
}