package com.example.demo.domain.product.entity;

import com.example.demo.common.entity.BaseTimeEntity;
import com.example.demo.common.exception.CustomException;
import com.example.demo.common.exception.ErrorCode;
import com.example.demo.domain.category.entity.Category;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(
        name = "products",
        indexes = {
                @Index(name = "idx_products_category_status", columnList = "category_id, status")
        }
)
public class Product extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    @Column(nullable = false, length = 100)
    private String name;

    @Lob
    @Column(nullable = false)
    private String description;

    @Column(name = "thumbnail_url", length = 500)
    private String thumbnailUrl;

    @Column(nullable = false)
    private Long price;

    private Integer stock;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private ProductStatus status;

    public Product(Category category, String name, String description,
                   String thumbnailUrl, Long price, Integer stock) {
        this.category = category;
        this.name = name;
        this.description = description;
        this.thumbnailUrl = thumbnailUrl;
        this.price = price;
        this.stock = stock;
        this.status = ProductStatus.ON_SALE;
    }

    public boolean isPurchasable() {
        return this.status == ProductStatus.ON_SALE
                && this.stock != null && this.stock > 0;
    }

    public void updateStatus(ProductStatus status){
        this.status = status;
    }

    public boolean isHidden() {
        return this.status == ProductStatus.HIDDEN;
    }

    public void updateCategory(Category category){
        this.category = category;
    }

    public void updateName(String name){
        this.name = name;
    }

    public void updateDescription(String description){
        this.description = description;
    }

    public void updateThumbnail(String thumbnailUrl){
        this.thumbnailUrl = thumbnailUrl;
    }

    public void updatePrice(Long price){
        this.price = price;
    }

    public void updateStock(Integer stock){
        if(stock < 0){
            throw new CustomException(ErrorCode.INVALID_QUANTITY);
        }
        this.stock = stock;
    }

    public void increaseStock(int quantity) {
        this.stock += quantity;
    }

    public void decreaseStock(int quantity) {
        this.stock -= quantity;
    }
}