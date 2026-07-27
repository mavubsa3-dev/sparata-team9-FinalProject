package com.example.demo.domain.category.dto.response;

import com.example.demo.domain.category.entity.Category;

public record CreateCategoryResponse(
	Long id,
	String name
) {
	public static CreateCategoryResponse from(Category category){
		return new CreateCategoryResponse(
			category.getId(),
			category.getName() + " 카테고리를 추가했습니다."
		);
	}
}
