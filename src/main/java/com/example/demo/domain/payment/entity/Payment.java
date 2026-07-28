package com.example.demo.domain.payment.entity;

import com.example.demo.common.entity.BaseTimeEntity;
import com.example.demo.domain.order.entity.Order;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "payments")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Payment extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false, unique = true)
    private Order order;

    @Column(name = "portone_payment_id", length = 100, unique = true)
    private String portonePaymentId;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 30)
    private PaymentStatus status;

    @Column(name = "total_product_amount", nullable = false)
    private Long totalProductAmount;

    @Column(name = "payment_amount", nullable = false)
    private Long paymentAmount;

    @Column(name = "approved_at")
    private LocalDateTime approvedAt;

    @Column(name = "canceled_at")
    private LocalDateTime canceledAt;

    public Payment(Order order, Long totalProductAmount, Long paymentAmount) {
        this.order = order;
        this.status = PaymentStatus.PENDING;
        this.totalProductAmount = totalProductAmount;
        this.paymentAmount = paymentAmount;
    }

    public void cancel() {
        this.status = PaymentStatus.CANCELED;
        this.canceledAt = LocalDateTime.now();
    }
}