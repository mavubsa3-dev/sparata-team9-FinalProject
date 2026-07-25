package com.example.demo.domain.product.service;

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
import com.example.demo.domain.product.dto.response.UpdateProductResponse;
import com.example.demo.domain.product.entity.Product;
import com.example.demo.domain.product.repository.ProductRepository;
import com.example.demo.domain.user.entity.User;
import com.example.demo.domain.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class AdminProductService {

	private final ProductRepository productRepository;
	private final CategoryRepository categoryRepository;
	private final UserRepository userRepository;

	@Transactional
	public CreateProductResponse createProduct(Long adminId, CreateProductRequest request){
		User admin = userRepository.findById(adminId).orElseThrow(
			() -> new CustomException(ErrorCode.ADMIN_NOT_FOUND)
		);

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
		log.info("[상품 생성] 생성된 상품 : {} , 카테고리 : {}, 생성한 관리자 : {} ", product.getName(), product.getCategory().getName(), admin.getName());

		return CreateProductResponse.from(savedProduct);
	}

	@Transactional
	public UpdateProductResponse updateProduct(Long adminId, Long productId, UpdateProductRequest request){
		User admin = userRepository.findById(adminId).orElseThrow(
			() -> new CustomException(ErrorCode.ADMIN_NOT_FOUND)
		);

		Product product = productRepository.findByIdWithCategory(productId)
			.orElseThrow(() -> new CustomException(ErrorCode.PRODUCT_NOT_FOUND));

		if (request.categoryId() != null){
			Category newCategory = categoryRepository.findById(request.categoryId()).orElseThrow(
				() -> new CustomException(ErrorCode.CATEGORY_NOT_FOUND)
			);

			product.updateCategory(newCategory);
		}

		validAndUpdateProduct(product, request);

		log.info("[상품 정보 업데이트] 업데이트 한 관리자 : {} ", admin.getName());

		return UpdateProductResponse.from(product);
	}

	@Transactional
	public DeleteProductResponse deleteProduct(Long adminId, Long productId){
		User admin = userRepository.findById(adminId).orElseThrow(
			() -> new CustomException(ErrorCode.ADMIN_NOT_FOUND)
		);

		Product product = productRepository.findByIdWithCategory(productId)
			.orElseThrow(() -> new CustomException(ErrorCode.PRODUCT_NOT_FOUND));

		productRepository.delete(product);

		log.info("[상품 삭제] 삭제된 상품 카테고리 : {}, 상품 이름 : {}, 삭제한 관리자 : {} ", product.getCategory().getName(), product.getName(), admin.getName());

		return DeleteProductResponse.from(product);
	}

	private void validAndUpdateProduct(Product product, UpdateProductRequest request){
		if (request.name() != null) product.updateName(request.name());
		if (request.description() != null) product.updateDescription(request.description());
		if (request.thumbnailUrl() != null) product.updateThumbnail(request.thumbnailUrl());
		if (request.price() != null) product.updatePrice(request.price());
		if (request.stock() != null) product.updateStock(request.stock());
		if (request.status() != null) product.updateStatus(request.status());
	}
}
