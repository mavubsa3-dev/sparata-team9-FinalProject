package com.example.demo.domain.product.dto.request;

import org.hibernate.validator.constraints.URL;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateProductRequest(
	@NotNull(message = "카테고리를 입력하세요")
	Long categoryId,

	@NotBlank(message = "상품 이름을 입력하세요")
	String name,

	@NotBlank(message = "상품 설명을 입력하세요")
	String description,

	@NotBlank(message = "상품 이미지를 등록하세요")
	@URL(message = "올바른 URL 형식으로 입력해주세요")
	String thumbnailUrl,

	@NotNull(message = "상품 가격을 입력하세요")
	@Min(value = 100, message = "가격은 100원 이상이어야 합니다.")
	Long price,

	@NotNull(message = "상품 수량을 입력하세요")
	@Min(value = 1, message = "수량은 1개 이상이어야 합니다")
	Integer stock
) {
}
