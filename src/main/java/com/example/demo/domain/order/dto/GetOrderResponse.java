package com.example.demo.domain.order.dto;

import com.example.demo.domain.order.entity.Order;

import java.time.LocalDateTime;

public record GetOrderResponse(
        Long orderId,
        String orderNumber,
        String status,
        Long totalProductAmount,
        LocalDateTime createdAt
) {
    public static GetOrderResponse from(Order order) {
        return new GetOrderResponse(
                order.getId(),
                order.getOrderNumber(),
                order.getStatus().name(),
                order.getTotalProductAmount(),
                order.getCreatedAt()
        );
    }
}