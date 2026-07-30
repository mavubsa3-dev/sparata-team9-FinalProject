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
        boolean available = !cartItem.getProduct().isHidden();
        String productName = available ? cartItem.getProduct().getName() : "삭제된 상품입니다";

        long lineAmount = cartItem.getProduct().getPrice() * cartItem.getQuantity();

        return new CartItemResponse(
                cartItem.getId(),
                cartItem.getProduct().getId(),
                productName,
                cartItem.getProduct().getThumbnailUrl(),
                cartItem.getProduct().getPrice(),
                cartItem.getQuantity(),
                lineAmount
        );
    }
}