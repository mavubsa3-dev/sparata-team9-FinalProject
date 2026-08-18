package com.example.demo.domain.product.dto.response;

public record ImageUploadResponse(
        String imageUrl
) {
    public static ImageUploadResponse from(String imageUrl) {
        return new ImageUploadResponse(imageUrl);
    }
}