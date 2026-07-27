package com.example.demo.domain.product.dto.request;

import org.hibernate.validator.constraints.URL;
import com.example.demo.domain.product.entity.ProductStatus;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;

public record UpdateProductRequest(
	Long categoryId,

	@Size(min = 1, message = "상품 이름은 최소 1자 이상이어야 합니다.")
	String name,

	@Size(min = 1, message = "상품 설명을 입력해주세요.")
	String description,

	@URL(message = "올바른 URL 형식으로 입력해주세요")
	String thumbnailUrl,

	@Min(value = 100, message = "가격은 100원 이상이어야 합니다.")
	Long price,

	@Min(value = 0, message = "수량은 음수가 될 수 없습니다.")
	Integer stock,

	ProductStatus status
) {
}
