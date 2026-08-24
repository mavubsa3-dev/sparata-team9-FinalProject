package com.example.demo.domain.payment.repository;

import com.example.demo.domain.payment.entity.Payment;
import com.example.demo.domain.payment.entity.PaymentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.List;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

    Optional<Payment> findByOrderId(Long orderId);

    boolean existsByOrderId(Long orderId);

    @Query("""
            SELECT
                COALESCE(SUM(p.paymentAmount), 0) AS totalAmount,
                COUNT(p) AS orderCount
            FROM Payment p
            WHERE p.status = :status
                AND p.approvedAt >= :start
                AND p.approvedAt < :end
            """)
    PaymentSettlementSummary getDailySettlementSummary(
            @Param("status") PaymentStatus status,
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end
    );

    @Query("""
            SELECT p FROM Payment p
            JOIN FETCH p.order o
            JOIN FETCH o.user
            LEFT JOIN FETCH o.orderItems
            WHERE p.id = :paymentId
            """)
    Optional<Payment> findDetailById(@Param("paymentId") Long paymentId);


    @Query("""
            SELECT p FROM Payment p
            JOIN FETCH p.order o
            LEFT JOIN FETCH o.orderItems            
            WHERE o.user.id = :userId
            ORDER BY p.createdAt DESC
            """)
    List<Payment> findAllByUserIdOrderByCreatedAtDesc(@Param("userId") Long userId);

    @Query("""
            SELECT p FROM Payment p
            JOIN FETCH p.order o
            LEFT JOIN FETCH o.orderItems            
            ORDER BY p.createdAt DESC
            """)
    List<Payment> findAllOrderByCreatedAtDesc();
}