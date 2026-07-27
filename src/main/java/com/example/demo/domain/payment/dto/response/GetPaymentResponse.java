package com.example.demo.domain.payment.dto.response;

import com.example.demo.domain.payment.entity.Payment;
import com.example.demo.domain.payment.entity.PaymentStatus;

import java.time.LocalDateTime;

public record GetPaymentResponse(
        Long paymentId,
        Long orderId,
        String portonePaymentId,
        PaymentStatus status,
        Long totalProductAmount,
        Long paymentAmount,
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
                payment.getApprovedAt(),
                payment.getCanceledAt(),
                payment.getCreatedAt()
        );
    }
}