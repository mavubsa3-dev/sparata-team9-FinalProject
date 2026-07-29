package com.example.demo.domain.payment.repository;

import com.example.demo.domain.payment.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.List;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

    Optional<Payment> findByOrderId(Long orderId);

    boolean existsByOrderId(Long orderId);

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