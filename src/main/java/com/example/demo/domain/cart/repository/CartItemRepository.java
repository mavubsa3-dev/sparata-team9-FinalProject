package com.example.demo.domain.cart.repository;

import com.example.demo.domain.cart.entity.Cart;
import com.example.demo.domain.cart.entity.CartItem;
import com.example.demo.domain.product.entity.Product;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    @Query("""
            SELECT ci FROM CartItem ci LEFT JOIN FETCH ci.product
            WHERE ci.cart.id = :cartId
            ORDER BY ci.id DESC
            """)
    List<CartItem> findByCartIdWithProduct(@Param("cartId") Long cartId);

    Optional<CartItem> findByCartAndProduct(Cart cart, Product product);

    @Query("""
            SELECT ci FROM CartItem ci LEFT JOIN FETCH ci.product
            WHERE ci.id = :id
            """)
    Optional<CartItem> findByIdWithProduct(@Param("id") Long id);

    @Modifying(clearAutomatically = true)
    @Query("DELETE FROM CartItem ci WHERE ci.cart.id = :cartId")
    void deleteAllByCartId(@Param("cartId") Long cartId);
}