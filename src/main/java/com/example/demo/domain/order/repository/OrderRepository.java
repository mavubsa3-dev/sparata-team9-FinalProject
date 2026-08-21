package com.example.demo.domain.order.repository;

import com.example.demo.domain.order.entity.Order;
import com.example.demo.domain.order.entity.OrderStatus;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {

    Optional<Order> findByOrderNumber(String orderNumber);

    boolean existsByOrderNumber(String orderNumber);

    List<Order> findAllByUserIdOrderByCreatedAtDesc(Long userId);

    @Query("""
            SELECT o FROM Order o
            LEFT JOIN FETCH o.orderItems oi
            LEFT JOIN FETCH oi.product
            WHERE o.id = :orderId AND o.user.id = :userId
            """)
    Optional<Order> findDetailByIdAndUserId(@Param("orderId") Long orderId, @Param("userId") Long userId);

    @Query("""
        SELECT o FROM Order o
        LEFT JOIN FETCH o.orderItems oi
        LEFT JOIN FETCH oi.product
        WHERE o.status = :status AND o.createdAt < :dateTime
        """)
    List<Order> findAllByStatusAndCreatedAtBefore(
            @Param("status") OrderStatus status,
            @Param("dateTime") LocalDateTime dateTime
    );

    @Query("""
            SELECT o.id FROM Order o
            WHERE o.status = :status AND o.createdAt < :before
            """)
    List<Long> findIdsByStatusAndCreatedAtBefore(
            @Param("status") OrderStatus status,
            @Param("before") LocalDateTime before
    );

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT o FROM Order o WHERE o.id = :id")
    Optional<Order> findByIdForUpdate(@Param("id") Long id);
}