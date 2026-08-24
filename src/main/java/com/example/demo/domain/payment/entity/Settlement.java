package com.example.demo.domain.payment.entity;

import com.example.demo.common.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * 결제완료(PAID) 데이터를 날짜 단위로 집계한 결과를 저장하는 엔티티.
 * Payment와 직접적인 연관관계(FK)는 없다 — 날짜 범위로 집계만 하기 때문.
 */
@Entity
@Table(name = "settlements", uniqueConstraints = {
        @UniqueConstraint(name = "uk_settlement_date", columnNames = "settlement_date")
})
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Settlement extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "settlement_date", nullable = false)
    private LocalDate settlementDate;

    @Column(name = "total_amount", nullable = false)
    private Long totalAmount;

    @Column(name = "order_count", nullable = false)
    private Long orderCount;

    public Settlement(LocalDate settlementDate, Long totalAmount, Long orderCount) {
        this.settlementDate = settlementDate;
        this.totalAmount = totalAmount;
        this.orderCount = orderCount;
    }

    public void update(Long totalAmount, Long orderCount) {
        this.totalAmount = totalAmount;
        this.orderCount = orderCount;
    }
}