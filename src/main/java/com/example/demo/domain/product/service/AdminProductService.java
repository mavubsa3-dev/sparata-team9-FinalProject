package com.example.demo.domain.product.service;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.common.exception.CustomException;
import com.example.demo.common.exception.ErrorCode;
import com.example.demo.domain.category.entity.Category;
import com.example.demo.domain.category.repository.CategoryRepository;
import com.example.demo.domain.product.dto.request.CreateProductRequest;
import com.example.demo.domain.product.dto.request.UpdateProductRequest;
import com.example.demo.domain.product.dto.response.CreateProductResponse;
import com.example.demo.domain.product.dto.response.DeleteProductResponse;
import com.example.demo.domain.product.dto.response.GetProductFromAdminResponse;
import com.example.demo.domain.product.dto.response.UpdateProductResponse;
import com.example.demo.domain.product.entity.Product;
import com.example.demo.domain.product.entity.ProductStatus;
import com.example.demo.domain.product.repository.ProductRepository;
import com.example.demo.domain.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AdminProductService {

	private final ProductRepository productRepository;
	private final CategoryRepository categoryRepository;
	private final UserRepository userRepository;

	@Transactional
	public CreateProductResponse createProduct(CreateProductRequest request){

		Category category = categoryRepository.findById(request.categoryId()).orElseThrow(
			() -> new CustomException(ErrorCode.CATEGORY_NOT_FOUND)
		);

		Product product = new Product(
			category,
			request.name(),
			request.description(),
			request.thumbnailUrl(),
			request.price(),
			request.stock()
		);

		Product savedProduct = productRepository.save(product);


		return CreateProductResponse.from(savedProduct);
	}

	@Transactional(readOnly = true)
	public Page<GetProductFromAdminResponse> getProduct(
		String category, String productName, Integer minPrice, Integer maxPrice, Integer stock, ProductStatus status, Pageable pageable){


		Specification<Product> spec = buildSpec(category, productName, minPrice, maxPrice, stock, status);

		return productRepository.findAll(spec, pageable)
			.map(GetProductFromAdminResponse::from);
	}

	@Transactional
	public UpdateProductResponse updateProduct(Long productId, UpdateProductRequest request){


		Product product = productRepository.findByIdWithCategory(productId)
			.orElseThrow(() -> new CustomException(ErrorCode.PRODUCT_NOT_FOUND));

		if (request.categoryId() != null){
			Category newCategory = categoryRepository.findById(request.categoryId()).orElseThrow(
				() -> new CustomException(ErrorCode.CATEGORY_NOT_FOUND)
			);

			product.updateCategory(newCategory);
		}

		validAndUpdateProduct(product, request);


		return UpdateProductResponse.from(product);
	}

	@CacheEvict(value = "Caffeine:product", key = "#productId")
	@Transactional
	public DeleteProductResponse deleteProduct(Long productId){

		Product product = productRepository.findByIdWithCategory(productId)
			.orElseThrow(() -> new CustomException(ErrorCode.PRODUCT_NOT_FOUND));

		product.updateStatus(ProductStatus.HIDDEN);


		return DeleteProductResponse.from(product);
	}

	private void validAndUpdateProduct(Product product, UpdateProductRequest request){
		if (request.name() != null && !request.name().isBlank()) product.updateName(request.name());
		if (request.description() != null && !request.description().isBlank()) product.updateDescription(request.description());
		if (request.thumbnailUrl() != null && !request.thumbnailUrl().isBlank()) product.updateThumbnail(request.thumbnailUrl());
		if (request.price() != null) product.updatePrice(request.price());
		if (request.stock() != null) product.updateStock(request.stock());
		if (request.status() != null) product.updateStatus(request.status());
	}

	private Specification<Product> buildSpec(
		String category, String productName,
		Integer minPrice, Integer maxPrice,
		Integer stock, ProductStatus status) {

		Specification<Product> spec = Specification.where((root, query, cb) -> cb.isTrue(cb.literal(true)));

		if (category != null && !category.isEmpty()) {
			spec = spec.and((root, query, cb) ->
				cb.equal(root.join("category").get("name"), category));
		}
		if (productName != null && !productName.isEmpty()) {
			spec = spec.and((root, query, cb) ->
				cb.like(root.get("name"), "%" + productName + "%"));
		}
		if (minPrice != null) {
			spec = spec.and((root, query, cb) ->
				cb.greaterThanOrEqualTo(root.get("price"), minPrice));
		}
		if (maxPrice != null) {
			spec = spec.and((root, query, cb) ->
				cb.lessThanOrEqualTo(root.get("price"), maxPrice));
		}
		if (stock != null) {
			spec = spec.and((root, query, cb) ->
				cb.greaterThanOrEqualTo(root.get("stock"), stock));
		}
		if (status != null) {
			spec = spec.and((root, query, cb) ->
				cb.equal(root.get("status"), status));
		}

		return spec;
	}
}
