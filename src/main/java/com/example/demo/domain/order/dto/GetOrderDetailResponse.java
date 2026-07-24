package com.example.demo.domain.order.dto;

import com.example.demo.domain.order.entity.Order;

import java.time.LocalDateTime;
import java.util.List;

public record GetOrderDetailResponse(
        Long orderId,
        String orderNumber,
        String status,
        Long totalProductAmount,
        String recipientName,
        String recipientPhone,
        String zipCode,
        String address1,
        String address2,
        LocalDateTime createdAt,
        List<OrderItemResponse> orderItems
) {
    public static GetOrderDetailResponse from(Order order) {
        List<OrderItemResponse> items = order.getOrderItems().stream()
                .map(OrderItemResponse::from)
                .toList();

        return new GetOrderDetailResponse(
                order.getId(),
                order.getOrderNumber(),
                order.getStatus().name(),
                order.getTotalProductAmount(),
                order.getRecipientName(),
                order.getRecipientPhone(),
                order.getZipCode(),
                order.getAddress1(),
                order.getAddress2(),
                order.getCreatedAt(),
                items
        );
    }
}