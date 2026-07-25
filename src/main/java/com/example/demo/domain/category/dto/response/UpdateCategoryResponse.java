package com.example.demo.domain.category.dto.response;

import com.example.demo.domain.category.entity.Category;

public record UpdateCategoryResponse(
	String name
) {
	public static UpdateCategoryResponse from(Category category){
		return new UpdateCategoryResponse(
			category.getName()
		);
	}
}
