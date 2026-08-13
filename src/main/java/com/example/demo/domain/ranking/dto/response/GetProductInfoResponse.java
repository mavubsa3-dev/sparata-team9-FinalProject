package com.example.demo.domain.ranking.dto.response;

import com.example.demo.domain.product.entity.Product;

public record GetProductInfoResponse(
	Long id,
	String name,
	String description,
	String thumbnailUrl,
	Long price,
	Integer stock,
	String status
) {
	public static GetProductInfoResponse from(Product product){
		return new GetProductInfoResponse(
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
