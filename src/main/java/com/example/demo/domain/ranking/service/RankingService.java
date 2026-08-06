package com.example.demo.domain.ranking.service;

import java.time.Duration;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.springframework.boot.web.error.Error;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.common.exception.CustomException;
import com.example.demo.common.exception.ErrorCode;
import com.example.demo.domain.product.entity.Product;
import com.example.demo.domain.product.repository.ProductRepository;
import com.example.demo.domain.ranking.dto.response.GetProductInfoResponse;
import com.example.demo.domain.ranking.dto.response.GetProductRankingResponse;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RankingService {

	// 저장 데이터 단순 문자열 -> StringRedisTemplate 사용
	private final StringRedisTemplate stringRedisTemplate;
	private static final String PRODUCT_RANKING_KEY = "product:ranking:";
	private final ProductRepository productRepository;

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
			.map(this::toResponse)
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
			.map(this::toResponse)
			.toList();
	}

	public GetProductInfoResponse getProductInRanking(Long productId){
		LocalDate currentDate = LocalDate.now();
		String key = PRODUCT_RANKING_KEY + currentDate;

		Set<String> rankingProducts = stringRedisTemplate.opsForZSet().range(key, 0, -1);
		boolean existsInRanking = rankingProducts != null && rankingProducts.stream()
			.anyMatch( value -> {
				String[] parts = value.split(":", 2);
				Long rankProductId = Long.parseLong(parts[0]);

				return rankProductId.equals(productId);
			});

		if(!existsInRanking){
			throw new CustomException(ErrorCode.PRODUCT_NOT_FOUND);
		}

		Product product = productRepository.findById(productId).orElseThrow(
			() -> new CustomException(ErrorCode.PRODUCT_NOT_FOUND)
		);

		return GetProductInfoResponse.from(product);
	}

	public GetProductInfoResponse getProductInWeekRanking(Long productId){
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

		Set<String> rankingProducts = stringRedisTemplate.opsForZSet().range(dKey, 0, -1);

		boolean existsInRanking = rankingProducts != null && rankingProducts.stream()
			.anyMatch( value -> {
				String[] parts = value.split(":", 2);
				Long rankProductId = Long.parseLong(parts[0]);

				return rankProductId.equals(productId);
			});

		if(!existsInRanking){
			throw new CustomException(ErrorCode.PRODUCT_NOT_FOUND);
		}

		Product product = productRepository.findById(productId).orElseThrow(
			() -> new CustomException(ErrorCode.PRODUCT_NOT_FOUND)
		);

		return GetProductInfoResponse.from(product);
	}

	private GetProductRankingResponse toResponse(ZSetOperations.TypedTuple<String> tuple){

		String value = tuple.getValue();
		double score = tuple.getScore();

		String[] parts = value.split(":", 2);

		Long id = Long.parseLong(parts[0]);
		String title = parts[1];

		return new GetProductRankingResponse(id, title, score);
	}
}
