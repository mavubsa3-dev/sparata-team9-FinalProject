package com.example.demo.domain.category.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.common.exception.CustomException;
import com.example.demo.common.exception.ErrorCode;
import com.example.demo.domain.category.dto.request.CreateCategoryRequest;
import com.example.demo.domain.category.dto.request.UpdateCategoryRequest;
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

		GetCategoryResponse response = GetCategoryResponse.from(savedCategory);
		try {
			categoryCacheService.saveCategoryCache(savedCategory.getId(), response);
		} catch (Exception e) {
			log.warn("Redis 캐시 저장 실패", e);
		}

		return CreateCategoryResponse.from(savedCategory);
	}

	@Transactional(readOnly = true)
	public List<GetCategoryResponse> getCategory(){
		List<Long> ids;
		try {
			ids = categoryRepository.findAll().stream()
					.map(Category::getId)
					.toList();
		} catch (Exception e) {
			throw e;
		}

		return ids.stream()
				.map(id -> {
					GetCategoryResponse cached = null;
					try {
						cached = categoryCacheService.getCategoryCache(id);
					} catch (Exception e) {
						log.warn("Redis 연결 실패, DB에서 직접 조회합니다.", e);
					}

					if (cached != null) {
						return cached;
					}

					Category category = categoryRepository.findById(id)
							.orElseThrow(() -> new CustomException(ErrorCode.CATEGORY_NOT_FOUND));
					GetCategoryResponse response = GetCategoryResponse.from(category);

					try {
						categoryCacheService.saveCategoryCache(id, response);
					} catch (Exception e) {
						log.warn("Redis 캐시 저장 실패", e);
					}

					return response;
				})
				.toList();
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

		try {
			categoryCacheService.deleteCategoryCache(categoryId);
		} catch (Exception e) {
			log.warn("Redis 캐시 삭제 실패", e);
		}

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

		try {
			categoryCacheService.deleteCategoryCache(categoryId);
		} catch (Exception e) {
			log.warn("Redis 캐시 삭제 실패", e);
		}

		return DeleteCategoryResponse.from(category);
	}
}