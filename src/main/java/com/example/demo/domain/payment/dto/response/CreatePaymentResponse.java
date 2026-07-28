package com.example.demo.domain.payment.dto.response;

import com.example.demo.domain.payment.entity.Payment;
import com.example.demo.domain.payment.entity.PaymentStatus;

import java.time.LocalDateTime;

public record CreatePaymentResponse(
        Long paymentId,
        Long orderId,
        PaymentStatus status,
        Long totalProductAmount,
        Long paymentAmount,
        LocalDateTime createdAt
) {

    public static CreatePaymentResponse from(Payment payment) {
        return new CreatePaymentResponse(
                payment.getId(),
                payment.getOrder().getId(),
                payment.getStatus(),
                payment.getTotalProductAmount(),
                payment.getPaymentAmount(),
                payment.getCreatedAt()
        );
    }
}