package com.example.demo.domain.product.dto.response;

import com.example.demo.domain.product.entity.Product;


public record CreateProductResponse(
	Long categoryId,
	String name,
	String description,
	String thumbnailUrl,
	Long price,
	Integer stock
) {

	public static CreateProductResponse from(Product product){
		return new CreateProductResponse(
			product.getCategory().getId(),
			product.getName(),
			product.getDescription(),
			product.getThumbnailUrl(),
			product.getPrice(),
			product.getStock()
		);
	}
}
