package com.example.demo.domain.order.dto.response;

import com.example.demo.domain.order.entity.Order;
import com.example.demo.domain.order.entity.OrderStatus;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 주문 생성 응답 DTO.
 */
public record CreateOrderResponse(
        Long orderId,
        String orderNumber,
        OrderStatus status,
        Long totalProductAmount,
        List<OrderItemResponse> orderItems,
        LocalDateTime createdAt
) {
    public static CreateOrderResponse from(Order order) {
        return new CreateOrderResponse(
                order.getId(),
                order.getOrderNumber(),
                order.getStatus(),
                order.getTotalProductAmount(),
                order.getOrderItems().stream()
                        .map(OrderItemResponse::from)
                        .toList(),
                order.getCreatedAt()
        );
    }
}