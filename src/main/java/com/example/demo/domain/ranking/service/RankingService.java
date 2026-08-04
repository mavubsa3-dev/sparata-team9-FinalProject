package com.example.demo.domain.ranking.service;

import java.time.Duration;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;

import com.example.demo.domain.ranking.dto.response.GetProductRankingResponse;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RankingService {

	private final StringRedisTemplate stringRedisTemplate;
	private static final String PRODUCT_RANKING_KEY = "product:ranking:";

	public void increaseScore(String productInfo, int quantity){

		LocalDate currentDate = LocalDate.now();

		String key = PRODUCT_RANKING_KEY + currentDate;

		stringRedisTemplate.opsForZSet()
			.incrementScore(key, productInfo, quantity);

		stringRedisTemplate.expire(key, Duration.ofDays(8));
	}

	// 상위 N개 조회
	public List<GetProductRankingResponse> findProductTopNInToday(int count){

		LocalDate currentDate = LocalDate.now();

		String key = PRODUCT_RANKING_KEY + currentDate;

		Set<ZSetOperations.TypedTuple<String>> result = stringRedisTemplate.opsForZSet().reverseRangeWithScores(key, 0, count - 1);

		if(result == null){
			return Collections.emptyList();
		}

		return result.stream()
			.filter(tuple -> tuple.getValue() != null && tuple.getScore() != null)
			.map(tuple -> new GetProductRankingResponse(
				tuple.getValue(),
				tuple.getScore()
			))
			.toList();
	}

	// 1주일간 인기 상품 조회
	public List<GetProductRankingResponse> findProductTopNInLast7Days(int count){

		LocalDate currentDate = LocalDate.now();

		List<String> keys = List.of(
			PRODUCT_RANKING_KEY + currentDate,
			PRODUCT_RANKING_KEY + currentDate.minusDays(1),
			PRODUCT_RANKING_KEY + currentDate.minusDays(2),
			PRODUCT_RANKING_KEY + currentDate.minusDays(3),
			PRODUCT_RANKING_KEY + currentDate.minusDays(4),
			PRODUCT_RANKING_KEY + currentDate.minusDays(5),
			PRODUCT_RANKING_KEY + currentDate.minusDays(6)
		);

		String dKey = "product:ranking:last7days:" +  currentDate;

		stringRedisTemplate.opsForZSet().unionAndStore(keys.get(0), keys.subList(1, keys.size()), dKey);

		stringRedisTemplate.expire(dKey, Duration.ofDays(1));

		Set<ZSetOperations.TypedTuple<String>> result = stringRedisTemplate.opsForZSet().reverseRangeWithScores(dKey, 0, count-1);

		if(result == null){
			return Collections.emptyList();
		}

		return result.stream()
			.filter(tuple -> tuple.getValue() != null && tuple.getScore() != null)
			.map(tuple -> new GetProductRankingResponse(
				tuple.getValue(),
				tuple.getScore()
			))
			.toList();
	}
}
