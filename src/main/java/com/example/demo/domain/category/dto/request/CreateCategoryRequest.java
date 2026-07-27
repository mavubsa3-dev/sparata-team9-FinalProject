package com.example.demo.domain.category.dto.request;

import jakarta.validation.constraints.NotBlank;

public record CreateCategoryRequest(

	@NotBlank(message = "카테고리를 입력하세요.")
	String name
) {
}
