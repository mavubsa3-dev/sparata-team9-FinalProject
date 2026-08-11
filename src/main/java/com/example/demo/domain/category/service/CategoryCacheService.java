package com.example.demo.domain.category.service;

import com.example.demo.domain.category.dto.response.CategoryListResponse;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CategoryCacheService {

    private final RedisTemplate<String, Object> redisTemplate;

    private static final String CACHE_KEY = "categories";

    public void saveCategoryCache(CategoryListResponse categories) {
        redisTemplate.opsForValue().set(CACHE_KEY, categories, 24, TimeUnit.HOURS);
    }

    public CategoryListResponse getCategoryCache() {
        return (CategoryListResponse) redisTemplate.opsForValue().get(CACHE_KEY);
    }

    public void deleteCategoryCache() {
        redisTemplate.delete(CACHE_KEY);
    }
}