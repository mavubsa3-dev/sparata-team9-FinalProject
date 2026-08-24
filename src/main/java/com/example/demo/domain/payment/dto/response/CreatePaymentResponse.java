package com.example.demo.domain.payment.dto.response;

import com.example.demo.domain.payment.entity.Payment;
import com.example.demo.domain.payment.entity.PaymentStatus;

import java.time.LocalDateTime;
import java.util.List;

public record CreatePaymentResponse(
        Long paymentId,
        Long orderId,
        PaymentStatus status,
        Long totalProductAmount,
        Long paymentAmount,
        String recipientName,
        String recipientPhone,
        String zipCode,
        String address1,
        String address2,
        List<PaymentItemResponse> items,
        LocalDateTime createdAt
) {

    public static CreatePaymentResponse from(Payment payment) {
        return new CreatePaymentResponse(
                payment.getId(),
                payment.getOrder().getId(),
                payment.getStatus(),
                payment.getTotalProductAmount(),
                payment.getPaymentAmount(),
                payment.getOrder().getRecipientName(),
                payment.getOrder().getRecipientPhone(),
                payment.getOrder().getZipCode(),
                payment.getOrder().getAddress1(),
                payment.getOrder().getAddress2(),
                payment.getOrder().getOrderItems().stream()
                        .map(PaymentItemResponse::from)
                        .toList(),
                payment.getCreatedAt()
        );
    }
}