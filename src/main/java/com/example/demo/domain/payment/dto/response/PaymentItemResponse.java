package com.example.demo.domain.payment.dto.response;

import com.example.demo.domain.order.entity.OrderItem;

public record PaymentItemResponse(
        String productName,
        Integer quantity
) {

    public static PaymentItemResponse from(OrderItem orderItem) {
        return new PaymentItemResponse(
                orderItem.getProductName(),
                orderItem.getQuantity()
        );
    }
}