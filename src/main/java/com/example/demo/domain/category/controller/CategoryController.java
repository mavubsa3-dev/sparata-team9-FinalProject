package com.example.demo.domain.category.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.domain.category.dto.request.CreateCategoryRequest;
import com.example.demo.domain.category.dto.request.UpdateCategoryRequest;
import com.example.demo.domain.category.dto.response.CategoryListResponse;
import com.example.demo.domain.category.dto.response.CreateCategoryResponse;
import com.example.demo.domain.category.dto.response.DeleteCategoryResponse;
import com.example.demo.domain.category.dto.response.UpdateCategoryResponse;
import com.example.demo.domain.category.service.CategoryService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/categories")
public class CategoryController {

	private final CategoryService categoryService;

	@PostMapping
	public ResponseEntity<CreateCategoryResponse> createCategory(@AuthenticationPrincipal Long adminId, @RequestBody CreateCategoryRequest request){
		return ResponseEntity.status(HttpStatus.CREATED).body(categoryService.createCategory(adminId, request));
	}

	@GetMapping
	public ResponseEntity<CategoryListResponse> getCategory(){
		return ResponseEntity.status(HttpStatus.OK).body(categoryService.getCategory());
	}

	@PatchMapping("/{categoryId}")
	public ResponseEntity<UpdateCategoryResponse> updateCategory(@PathVariable Long categoryId, @AuthenticationPrincipal Long adminId, @RequestBody
	UpdateCategoryRequest request){
		return ResponseEntity.status(HttpStatus.OK).body(categoryService.updateCategory(categoryId, request, adminId));
	}

	@DeleteMapping("/{categoryId}")
	public ResponseEntity<DeleteCategoryResponse> deleteCategory(@PathVariable Long categoryId, @AuthenticationPrincipal Long adminId){
		return ResponseEntity.status(HttpStatus.OK).body(categoryService.deleteCategory(categoryId, adminId));
	}

}