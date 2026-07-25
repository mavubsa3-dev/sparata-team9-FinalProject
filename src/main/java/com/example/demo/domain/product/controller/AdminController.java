package com.example.demo.domain.product.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.domain.product.dto.request.CreateProductRequest;
import com.example.demo.domain.product.dto.request.UpdateProductRequest;
import com.example.demo.domain.product.dto.response.CreateProductResponse;
import com.example.demo.domain.product.dto.response.DeleteProductResponse;
import com.example.demo.domain.product.dto.response.UpdateProductResponse;
import com.example.demo.domain.product.service.AdminProductService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/products")
public class AdminController {

	private final AdminProductService adminProductService;

	@PostMapping
	public ResponseEntity<CreateProductResponse> createProduct(@AuthenticationPrincipal Long adminId, @Valid @RequestBody
		CreateProductRequest request){

		return ResponseEntity.status(HttpStatus.CREATED).body(adminProductService.createProduct(adminId, request));
	}

	@PatchMapping("/{productId}")
	public ResponseEntity<UpdateProductResponse> updateProduct(@AuthenticationPrincipal Long adminId, @PathVariable Long productId,
		@Valid @RequestBody UpdateProductRequest request){

		return ResponseEntity.status(HttpStatus.OK).body(adminProductService.updateProduct(adminId, productId, request));
	}

	@DeleteMapping("/{productId}")
	public ResponseEntity<DeleteProductResponse> deleteProduct(@AuthenticationPrincipal Long adminId, @PathVariable Long productId){
		return ResponseEntity.status(HttpStatus.OK).body(adminProductService.deleteProduct(adminId, productId));
	}
}
