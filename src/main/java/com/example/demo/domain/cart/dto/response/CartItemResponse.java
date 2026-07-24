package com.example.demo.domain.cart.dto.response;

import com.example.demo.domain.cart.entity.CartItem;

public record CartItemResponse(
        Long cartItemId,
        Long productId,
        String productName,
        String thumbnailUrl,
        Long price,
        Integer quantity,
        Long lineAmount
) {
    public static CartItemResponse from(CartItem cartItem) {
        long lineAmount = cartItem.getProduct().getPrice() * cartItem.getQuantity();
        return new CartItemResponse(
                cartItem.getId(),
                cartItem.getProduct().getId(),
                cartItem.getProduct().getName(),
                cartItem.getProduct().getThumbnailUrl(),
                cartItem.getProduct().getPrice(),
                cartItem.getQuantity(),
                lineAmount
        );
    }
}