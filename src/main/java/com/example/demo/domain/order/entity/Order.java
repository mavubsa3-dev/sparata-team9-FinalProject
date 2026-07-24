package com.example.demo.domain.order.entity;

import com.example.demo.common.entity.BaseTimeEntity;
import com.example.demo.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Order extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "order_number", nullable = false, unique = true, length = 50)
    private String orderNumber;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 30)
    private OrderStatus status;

    @Column(name = "total_product_amount", nullable = false)
    private Long totalProductAmount;

    // 주문 시점 배송지 스냅샷
    @Column(name = "recipient_name", length = 20)
    private String recipientName;

    @Column(name = "recipient_phone", length = 20)
    private String recipientPhone;

    @Column(name = "zip_code", length = 10)
    private String zipCode;

    @Column(name = "address1", length = 200)
    private String address1;

    @Column(name = "address2", length = 200)
    private String address2;

    @Column(name = "canceled_at")
    private LocalDateTime canceledAt;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> orderItems = new ArrayList<>();

    public Order(
            User user,
            String orderNumber,
            String recipientName,
            String recipientPhone,
            String zipCode,
            String address1,
            String address2
    ) {
        this.user = user;
        this.orderNumber = orderNumber;
        this.status = OrderStatus.PAYMENT_PENDING;
        this.totalProductAmount = 0L;
        this.recipientName = recipientName;
        this.recipientPhone = recipientPhone;
        this.zipCode = zipCode;
        this.address1 = address1;
        this.address2 = address2;
    }

    public void addOrderItem(OrderItem orderItem) {
        this.orderItems.add(orderItem);
        this.totalProductAmount += orderItem.getLineAmount();
    }

    public void cancel() {
        this.status = OrderStatus.CANCELED;
        this.canceledAt = LocalDateTime.now();
    }

    public void complete() {
        this.status = OrderStatus.COMPLETED;
    }

    public boolean isPaymentPending() {
        return this.status == OrderStatus.PAYMENT_PENDING;
    }
}