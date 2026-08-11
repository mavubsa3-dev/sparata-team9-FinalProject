package com.example.demo.domain.category.dto.response;

import java.util.List;

public record CategoryListResponse(
        List<GetCategoryResponse> categories
) {
    public static CategoryListResponse from(List<GetCategoryResponse> categories) {
        return new CategoryListResponse(categories);
    }
}