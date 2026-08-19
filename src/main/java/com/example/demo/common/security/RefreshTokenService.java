package com.example.demo.common.security;

import java.time.Duration;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

	private final StringRedisTemplate stringRedisTemplate;
	private final String REFRESH_TOKEN_KEY = "refresh:token:";


	// 토큰 저장
	public void save(Long userId, String refreshToken, long expireMills) {

		String key = REFRESH_TOKEN_KEY + userId;
		stringRedisTemplate.opsForValue().set(
			key,
			refreshToken,
			Duration.ofMillis(expireMills)
		);
	}

	// 조회
	public String findByUserId(Long userId) {

		String key = REFRESH_TOKEN_KEY + userId;
		return stringRedisTemplate.opsForValue().get(key);
	}

	// 삭제(로그아웃)
	public void deleteByUserId(Long userId) {

		String key = REFRESH_TOKEN_KEY + userId;
		stringRedisTemplate.delete(key);
	}
}
