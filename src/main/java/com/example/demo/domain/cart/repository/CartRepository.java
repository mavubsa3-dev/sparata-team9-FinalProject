package com.example.demo.domain.cart.repository;

import com.example.demo.domain.cart.entity.Cart;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CartRepository extends JpaRepository<Cart, Long> {

    @Query("SELECT c FROM Cart c LEFT JOIN FETCH c.user WHERE c.user.id = :userId")
    Optional<Cart> findByUserId(@Param("userId") Long userId);
}