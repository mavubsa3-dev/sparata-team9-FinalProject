package com.example.demo.domain.category.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.domain.category.dto.response.GetCategoryResponse;
import com.example.demo.domain.category.service.CategoryService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/categories")
public class CategoryQueryController {

    private final CategoryService categoryService;

    @GetMapping
    public ResponseEntity<List<GetCategoryResponse>> getCategories() {
        return ResponseEntity.status(HttpStatus.OK).body(categoryService.getCategory());
    }
}