package com.example.demo.domain.category.dto.response;

import com.example.demo.domain.category.entity.Category;

public record DeleteCategoryResponse(
	String name
) {
	public static DeleteCategoryResponse from(Category category){
		return new DeleteCategoryResponse(
			category.getName() + " 카테고리를 삭제했습니다."
		);
	}
}
