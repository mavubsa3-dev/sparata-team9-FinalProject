package com.example.demo.domain.order.dto.response;

import com.example.demo.domain.order.entity.OrderItem;

public record OrderItemResponse(
        Long orderItemId,
        Long productId,
        String productName,
        Long unitPrice,
        Integer quantity,
        Long lineAmount
) {
    public static OrderItemResponse from(OrderItem orderItem) {
        return new OrderItemResponse(
                orderItem.getId(),
                orderItem.getProduct().getId(),
                orderItem.getProductName(),
                orderItem.getUnitPrice(),
                orderItem.getQuantity(),
                orderItem.getLineAmount()
        );
    }
}