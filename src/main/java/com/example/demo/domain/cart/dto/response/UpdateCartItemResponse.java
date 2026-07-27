package com.example.demo.domain.cart.dto.response;

import com.example.demo.domain.cart.entity.CartItem;

public record UpdateCartItemResponse(
        Long cartItemId,
        Integer quantity
) {
    public static UpdateCartItemResponse from(CartItem cartItem) {
        return new UpdateCartItemResponse(
                cartItem.getId(),
                cartItem.getQuantity()
        );
    }
}