package com.example.demo.domain.category.dto.request;

import jakarta.validation.constraints.NotBlank;

public record UpdateCategoryRequest(

	@NotBlank(message = "카테고리 이름을 입력하세요")
	String name
) {
}
