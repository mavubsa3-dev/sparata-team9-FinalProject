package com.example.demo.domain.product.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.domain.product.dto.request.CreateProductRequest;
import com.example.demo.domain.product.dto.request.UpdateProductRequest;
import com.example.demo.domain.product.dto.response.CreateProductResponse;
import com.example.demo.domain.product.dto.response.DeleteProductResponse;
import com.example.demo.domain.product.dto.response.GetProductFromAdminResponse;
import com.example.demo.domain.product.dto.response.UpdateProductResponse;
import com.example.demo.domain.product.entity.ProductStatus;
import com.example.demo.domain.product.service.AdminProductService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/products")
public class AdminController {

	private final AdminProductService adminProductService;

	@PostMapping
	public ResponseEntity<CreateProductResponse> createProduct(@Valid @RequestBody
		CreateProductRequest request){

		return ResponseEntity.status(HttpStatus.CREATED).body(adminProductService.createProduct(request));
	}

	@GetMapping
	public ResponseEntity<Page<GetProductFromAdminResponse>> getProduct(
		@RequestParam(required = false) String category,
		@RequestParam(required = false) String productName,
		@RequestParam(required = false) Integer minPrice,
		@RequestParam(required = false) Integer maxPrice,
		@RequestParam(required = false) Integer stock,
		@RequestParam(required = false) ProductStatus status,

		@PageableDefault(page = 0, size = 10, sort = "name", direction = Sort.Direction.ASC) Pageable pageable){

		return ResponseEntity.status(HttpStatus.OK)
			.body(adminProductService.getProduct(category, productName, minPrice, maxPrice, stock, status, pageable));
	}

	@PatchMapping("/{productId}")
	public ResponseEntity<UpdateProductResponse> updateProduct(@PathVariable Long productId,
		@Valid @RequestBody UpdateProductRequest request){

		return ResponseEntity.status(HttpStatus.OK).body(adminProductService.updateProduct(productId, request));
	}

	@DeleteMapping("/{productId}")
	public ResponseEntity<DeleteProductResponse> deleteProduct(@AuthenticationPrincipal Long adminId, @PathVariable Long productId){
		return ResponseEntity.status(HttpStatus.OK).body(adminProductService.deleteProduct(productId));
	}
}
