package com.example.demo.domain.cart.dto.response;

import com.example.demo.domain.cart.entity.CartItem;

public record AddCartItemResponse(
        Long cartItemId,
        Long productId,
        Integer quantity
) {
    public static AddCartItemResponse from(CartItem cartItem) {
        return new AddCartItemResponse(
                cartItem.getId(),
                cartItem.getProduct().getId(),
                cartItem.getQuantity()
        );
    }
}