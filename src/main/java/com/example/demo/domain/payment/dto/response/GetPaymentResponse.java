package com.example.demo.domain.payment.dto.response;

import com.example.demo.domain.payment.entity.Payment;
import com.example.demo.domain.payment.entity.PaymentStatus;

import java.time.LocalDateTime;
import java.util.List;


public record GetPaymentResponse(
        Long paymentId,
        Long orderId,
        String portonePaymentId,
        PaymentStatus status,
        Long totalProductAmount,
        Long paymentAmount,
        List<PaymentItemResponse> items,
        LocalDateTime approvedAt,
        LocalDateTime canceledAt,
        LocalDateTime createdAt
) {

    public static GetPaymentResponse from(Payment payment) {
        return new GetPaymentResponse(
                payment.getId(),
                payment.getOrder().getId(),
                payment.getPortonePaymentId(),
                payment.getStatus(),
                payment.getTotalProductAmount(),
                payment.getPaymentAmount(),
                payment.getOrder().getOrderItems().stream()
                        .map(PaymentItemResponse::from)
                        .toList(),
                payment.getApprovedAt(),
                payment.getCanceledAt(),
                payment.getCreatedAt()
        );
    }
}