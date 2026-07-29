package com.example.demo.domain.product.dto.response;

import com.example.demo.domain.product.entity.Product;

public record GetOneProductResponse(
        Long id,
        String name,
        String description,
        String thumbnailUrl,
        Long price,
        Integer stock,
        String status
) {
    public static GetOneProductResponse from(Product product) {
        return new GetOneProductResponse(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getThumbnailUrl(),
                product.getPrice(),
                product.getStock(),
                product.getStatus().name()
        );
    }
}