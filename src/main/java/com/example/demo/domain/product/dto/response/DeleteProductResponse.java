package com.example.demo.domain.product.dto.response;

import com.example.demo.domain.product.entity.Product;

public record DeleteProductResponse(
	String categoryName,
	String name
) {
	public static DeleteProductResponse from(Product product){
		return new DeleteProductResponse(
			product.getCategory().getName() + " 의",
			product.getName() + " 을 삭제했습니다"
		);
	}
}
