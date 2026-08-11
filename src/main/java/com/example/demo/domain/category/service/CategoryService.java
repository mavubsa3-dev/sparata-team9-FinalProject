package com.example.demo.domain.category.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.common.exception.CustomException;
import com.example.demo.common.exception.ErrorCode;
import com.example.demo.domain.category.dto.request.CreateCategoryRequest;
import com.example.demo.domain.category.dto.request.UpdateCategoryRequest;
import com.example.demo.domain.category.dto.response.CategoryListResponse;
import com.example.demo.domain.category.dto.response.CreateCategoryResponse;
import com.example.demo.domain.category.dto.response.DeleteCategoryResponse;
import com.example.demo.domain.category.dto.response.GetCategoryResponse;
import com.example.demo.domain.category.dto.response.UpdateCategoryResponse;
import com.example.demo.domain.category.entity.Category;
import com.example.demo.domain.category.repository.CategoryRepository;
import com.example.demo.domain.product.repository.ProductRepository;
import com.example.demo.domain.user.entity.User;
import com.example.demo.domain.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class CategoryService {

	private final CategoryRepository categoryRepository;
	private final UserRepository userRepository;
	private final ProductRepository productRepository;
	private final CategoryCacheService categoryCacheService;

	@Transactional
	public CreateCategoryResponse createCategory(Long adminId, CreateCategoryRequest request){
		User admin = userRepository.findById(adminId).orElseThrow(
				() -> new CustomException(ErrorCode.ADMIN_NOT_FOUND)
		);

		Category category = new Category(request.name(), admin);
		Category savedCategory = categoryRepository.save(category);

		log.info("생성된 카테고리 : {}, 생성한 관리자 : {} ", request.name(), admin.getName());
		categoryCacheService.deleteCategoryCache();

		return CreateCategoryResponse.from(savedCategory);
	}

	@Transactional(readOnly = true)
	public CategoryListResponse getCategory(){
		CategoryListResponse cached = categoryCacheService.getCategoryCache();
		if (cached != null) {
			return cached;
		}

		List<GetCategoryResponse> categories = categoryRepository.findAll().stream()
				.map(GetCategoryResponse::from)
				.toList();

		CategoryListResponse response = CategoryListResponse.from(categories);
		categoryCacheService.saveCategoryCache(response);
		return response;
	}

	@Transactional
	public UpdateCategoryResponse updateCategory(Long categoryId, UpdateCategoryRequest request, Long adminId){
		User admin = userRepository.findById(adminId).orElseThrow(
				() -> new CustomException(ErrorCode.ADMIN_NOT_FOUND)
		);

		Category category = categoryRepository.findById(categoryId).orElseThrow(
				() -> new CustomException(ErrorCode.CATEGORY_NOT_FOUND)
		);

		category.updateName(request.name(), admin);
		log.info("변경된 카테고리 이름 : {} , 변경한 관리자 : {} ", request.name(), admin.getName());
		categoryCacheService.deleteCategoryCache();

		return UpdateCategoryResponse.from(category);
	}

	@Transactional
	public DeleteCategoryResponse deleteCategory(Long categoryId, Long adminId) {

		User admin = userRepository.findById(adminId).orElseThrow(
				() -> new CustomException(ErrorCode.ADMIN_NOT_FOUND)
		);

		Category category = categoryRepository.findById(categoryId).orElseThrow(
				() -> new CustomException(ErrorCode.CATEGORY_NOT_FOUND)
		);


		if (productRepository.existsByCategoryId(categoryId)){
			throw new CustomException(ErrorCode.CATEGORY_HAS_PRODUCTS);
		}

		categoryRepository.delete(category);

		log.info("삭제된 카테고리 : {}, 삭제한 관리자 : {} ", category.getName(), admin.getName());
		categoryCacheService.deleteCategoryCache();

		return DeleteCategoryResponse.from(category);
	}
}