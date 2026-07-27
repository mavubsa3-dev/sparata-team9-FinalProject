package com.example.demo.domain.product.dto.response;

import com.example.demo.domain.product.entity.Product;

public record GetProductFromAdminResponse(
	Long id,
	String name,
	String description,
	String thumbnailUrl,
	Long price,
	Integer stock,
	String status,
	String categoryName
) {

	public static GetProductFromAdminResponse from(Product product){
		return new GetProductFromAdminResponse(
			product.getId(),
			product.getName(),
			product.getDescription(),
			product.getThumbnailUrl(),
			product.getPrice(),
			product.getStock(),
			product.getStatus().name(),
			product.getCategory().getName()
		);
	}
}
