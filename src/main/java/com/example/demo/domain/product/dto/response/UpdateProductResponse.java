package com.example.demo.domain.product.dto.response;

import com.example.demo.domain.product.entity.Product;
import com.example.demo.domain.product.entity.ProductStatus;

public record UpdateProductResponse(
	Long categoryId,
	String name,
	String description,
	String thumbnailUrl,
	Long price,
	Integer stock,
	ProductStatus status
) {
	public static UpdateProductResponse from(Product product){
		return new UpdateProductResponse(
			product.getCategory().getId(),
			product.getName(),
			product.getDescription(),
			product.getThumbnailUrl(),
			product.getPrice(),
			product.getStock(),
			product.getStatus()
		);
	}
}
