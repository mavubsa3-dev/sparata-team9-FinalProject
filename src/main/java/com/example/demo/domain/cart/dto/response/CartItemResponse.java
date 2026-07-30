// src/main/java/com/example/demo/domain/cart/dto/response/CartItemResponse.java
package com.example.demo.domain.cart.dto.response;

import com.example.demo.domain.cart.entity.CartItem;

public record CartItemResponse(
        Long cartItemId,
        Long productId,
        String productName,
        String thumbnailUrl,
        Long price,
        Integer quantity,
        Long lineAmount,
        String status
) {
    public static CartItemResponse from(CartItem cartItem) {
        String status = switch (cartItem.getProduct().getStatus()) {
            case ON_SALE -> "판매중";
            case SOLD_OUT -> "품절";
            case HIDDEN -> "삭제됨";
        };

        long lineAmount = cartItem.getProduct().getPrice() * cartItem.getQuantity();

        return new CartItemResponse(
                cartItem.getId(),
                cartItem.getProduct().getId(),
                cartItem.getProduct().getName(),
                cartItem.getProduct().getThumbnailUrl(),
                cartItem.getProduct().getPrice(),
                cartItem.getQuantity(),
                lineAmount,
                status
        );
    }
}