package com.example.demo.common.config.kafka.event;

import java.time.LocalDateTime;
import java.util.List;

import lombok.Builder;

@Builder
public record PaymentCompletedEvent(
	Long paymentId,
	Long userId,
	Long orderId,
	String orderNumber,
	String address,
	Long totalAmount,
	List<OrderItemInfo> orderItems,
	LocalDateTime completedAt
) { }
