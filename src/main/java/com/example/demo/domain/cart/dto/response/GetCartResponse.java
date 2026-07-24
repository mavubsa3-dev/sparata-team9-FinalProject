package com.example.demo.domain.cart.dto.response;

import com.example.demo.domain.cart.entity.Cart;
import com.example.demo.domain.cart.entity.CartItem;
import java.util.List;

public record GetCartResponse(
        Long cartId,
        List<CartItemResponse> items,
        Long totalAmount
) {
    public static GetCartResponse of(Cart cart, List<CartItem> cartItems) {
        List<CartItemResponse> itemResponses = cartItems.stream()
                .map(CartItemResponse::from)
                .toList();

        long totalAmount = itemResponses.stream()
                .mapToLong(CartItemResponse::lineAmount)
                .sum();

        return new GetCartResponse(cart.getId(), itemResponses, totalAmount);
    }
}