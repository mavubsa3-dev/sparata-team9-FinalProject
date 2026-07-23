package com.example.demo.domain.product.service;

import com.example.demo.common.exception.CustomException;
import com.example.demo.common.exception.ErrorCode;
import com.example.demo.domain.product.dto.response.GetOneProductResponse;
import com.example.demo.domain.product.dto.response.GetProductListResponse;
import com.example.demo.domain.product.entity.Product;
import com.example.demo.domain.product.entity.ProductStatus;
import com.example.demo.domain.product.repository.ProductRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class ProductService {

    private final ProductRepository productRepository;

    public List<GetProductListResponse> getProducts() {
        return productRepository.findAllByStatusNot(ProductStatus.HIDDEN)
                .stream()
                .map(GetProductListResponse::from)
                .toList();
    }

    public GetOneProductResponse getProduct(Long productId) {
        Product product = productRepository.findByIdWithCategory(productId)
                .orElseThrow(() -> new CustomException(ErrorCode.PRODUCT_NOT_FOUND));

        if (product.isHidden()) {
            throw new CustomException(ErrorCode.PRODUCT_NOT_FOUND);
        }

        return GetOneProductResponse.from(product);
    }
}