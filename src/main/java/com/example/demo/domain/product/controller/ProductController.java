package com.example.demo.domain.product.controller;

import com.example.demo.domain.product.dto.response.GetOneProductResponse;
import com.example.demo.domain.product.dto.response.GetProductListResponse;
import com.example.demo.domain.product.service.ProductService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    @GetMapping
    public ResponseEntity<List<GetProductListResponse>> getProducts(
            @RequestParam(required = false) Long categoryId
    ) {
        return ResponseEntity.ok(productService.getProducts(categoryId));
    }

    @GetMapping("/{productId}")
    public ResponseEntity<GetOneProductResponse> getProduct(@PathVariable Long productId) {
        return ResponseEntity.ok(productService.getProduct(productId));
    }
}