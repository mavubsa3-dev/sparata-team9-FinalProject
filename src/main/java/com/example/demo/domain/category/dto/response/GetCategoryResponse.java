package com.example.demo.domain.category.dto.response;

import com.example.demo.domain.category.entity.Category;

public record GetCategoryResponse(
	Long id,
	String name
) {

	public static GetCategoryResponse from(Category category){
		return new GetCategoryResponse(
			category.getId(),
			category.getName()
		);
	}
}
