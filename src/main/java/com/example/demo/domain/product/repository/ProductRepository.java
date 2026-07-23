package com.example.demo.domain.product.repository;

import com.example.demo.domain.product.entity.Product;
import com.example.demo.domain.product.entity.ProductStatus;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query("SELECT p FROM Product p LEFT JOIN FETCH p.category WHERE p.id = :id")
    Optional<Product> findByIdWithCategory(@Param("id") Long id);

    @Query("""
            SELECT p FROM Product p LEFT JOIN FETCH p.category
            WHERE p.status <> :status
            AND (:categoryId IS NULL OR p.category.id = :categoryId)
            ORDER BY p.id DESC
            """)
    List<Product> findProductsByFilter(
            @Param("status") ProductStatus status,
            @Param("categoryId") Long categoryId
    );
}