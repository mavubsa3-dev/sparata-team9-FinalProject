package com.example.demo.domain.cart.repository;

import com.example.demo.domain.cart.entity.CartItem;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    @Query("""
            SELECT ci FROM CartItem ci LEFT JOIN FETCH ci.product
            WHERE ci.cart.id = :cartId
            ORDER BY ci.id DESC
            """)
    List<CartItem> findByCartIdWithProduct(@Param("cartId") Long cartId);
}