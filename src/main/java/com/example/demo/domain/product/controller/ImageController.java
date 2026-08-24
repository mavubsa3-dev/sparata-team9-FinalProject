package com.example.demo.domain.product.controller;

import com.example.demo.domain.product.dto.response.ImageUploadResponse;
import com.example.demo.domain.product.service.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/products/images")
public class ImageController {

    private final ImageService imageService;

    @PostMapping
    public ResponseEntity<ImageUploadResponse> uploadImage(@RequestPart("file") MultipartFile file) {
        String imageUrl = imageService.uploadImage(file);
        return ResponseEntity.status(HttpStatus.OK).body(ImageUploadResponse.from(imageUrl));
    }
}