package com.example.demo.domain.category.service;

import com.example.demo.domain.category.dto.response.GetCategoryResponse;
import java.time.Duration;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CategoryCacheService {

    private static final String CACHE_KEY_PREFIX = "category:";
    private static final String ID_SET_KEY = "category:ids";

    private final RedisTemplate<String, Object> redisTemplate;

    public GetCategoryResponse getCategoryCache(Long id) {
        Object value = redisTemplate.opsForValue().get(CACHE_KEY_PREFIX + id);
        return (GetCategoryResponse) value;
    }

    public void saveCategoryCache(Long id, GetCategoryResponse response) {
        redisTemplate.opsForValue().set(CACHE_KEY_PREFIX + id, response, Duration.ofHours(24));
        redisTemplate.opsForSet().add(ID_SET_KEY, id);
    }

    public void deleteCategoryCache(Long id) {
        redisTemplate.delete(CACHE_KEY_PREFIX + id);
        redisTemplate.opsForSet().remove(ID_SET_KEY, id);
    }

    public Set<Object> getAllCategoryIds() {
        return redisTemplate.opsForSet().members(ID_SET_KEY);
    }
}