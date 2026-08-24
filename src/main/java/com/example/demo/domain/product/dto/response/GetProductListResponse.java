package com.example.demo.domain.product.dto.response;

import com.example.demo.domain.product.entity.Product;

public record GetProductListResponse(
        Long id,
        String name,
        String thumbnailUrl,
        Long price,
        String status,
        String categoryName
) {
    public static GetProductListResponse from(Product product) {
        return new GetProductListResponse(
                product.getId(),
                product.getName(),
                product.getThumbnailUrl(),
                product.getPrice(),
                product.getStatus().name(),
                product.getCategory() != null ? product.getCategory().getName() : null
        );
    }
}