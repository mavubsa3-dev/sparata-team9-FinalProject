package com.example.demo.domain.ranking.service;

import java.time.Duration;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;
import org.springframework.data.redis.core.HashOperations;
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
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RankingService {

	// 저장 데이터 단순 문자열 -> StringRedisTemplate 사용
	private final StringRedisTemplate stringRedisTemplate;
	private static final String PRODUCT_RANKING_KEY = "product:ranking:";
	private static final String RANKING_IDS_KEY = "ranking:products:ids:";
	private final ProductRepository productRepository;
	private final ObjectMapper objectMapper;
	private final AtomicLong cacheHitCount = new AtomicLong(0);
	private final AtomicLong cacheMissCount = new AtomicLong(0);
	private final AtomicLong dbQueryCount = new AtomicLong(0);

	// 상위 N개 조회
	public List<GetProductRankingResponse> findProductTopNInToday(int count){

		LocalDate currentDate = LocalDate.now();

		String key = PRODUCT_RANKING_KEY + currentDate;

		Set<ZSetOperations.TypedTuple<String>> result = stringRedisTemplate.opsForZSet().reverseRangeWithScores(key, 0, count - 1);

		if(result == null){
			return Collections.emptyList();
		}

		return toResponses(result);
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

		return toResponses(result);
	}

	public GetProductInfoResponse getProductInRanking(Long productId){

		boolean existsInRanking = findProductInTodayRanking(productId);

		if(!existsInRanking){
			throw new CustomException(ErrorCode.PRODUCT_NOT_FOUND);
		}

		return getProductFromCacheOrDB(productId);
	}


	public GetProductInfoResponse getProductInWeekRanking(Long productId){

		boolean existsInRanking = findProductInWeekRanking(productId);

		if(!existsInRanking){
			throw new CustomException(ErrorCode.PRODUCT_NOT_FOUND);
		}

		return getProductFromCacheOrDB(productId);
	}

	private boolean findProductInTodayRanking(Long productId){

		String key = RANKING_IDS_KEY + LocalDate.now();

		return stringRedisTemplate.opsForSet()
			.isMember(key, productId.toString());  // O(1) 조회
	}

	private boolean findProductInWeekRanking(Long productId){

		LocalDate currentDate = LocalDate.now();

		String dKey = "product:ranking:last7days:" +  currentDate;

		if (Boolean.FALSE.equals(stringRedisTemplate.hasKey(dKey))) {
			List<String> keys = List.of(
				PRODUCT_RANKING_KEY + currentDate,
				PRODUCT_RANKING_KEY + currentDate.minusDays(1),
				PRODUCT_RANKING_KEY + currentDate.minusDays(2),
				PRODUCT_RANKING_KEY + currentDate.minusDays(3),
				PRODUCT_RANKING_KEY + currentDate.minusDays(4),
				PRODUCT_RANKING_KEY + currentDate.minusDays(5),
				PRODUCT_RANKING_KEY + currentDate.minusDays(6)
			);
			stringRedisTemplate.opsForZSet()
				.unionAndStore(keys.get(0), keys.subList(1, keys.size()), dKey);
			stringRedisTemplate.expire(dKey, Duration.ofDays(1));
		}

		Set<String> rankingProducts = stringRedisTemplate.opsForZSet().range(dKey, 0, 99);

		if (rankingProducts == null || rankingProducts.isEmpty()) {
			return false;
		}

		return rankingProducts.stream()
			.anyMatch(v -> v.startsWith(productId + ":"));
	}

	private GetProductInfoResponse getProductFromCacheOrDB(Long productId){
		String key = "product:info:" + productId;
		HashOperations<String, String, String> hashOps = stringRedisTemplate.opsForHash();

		Map<String, String> hashCacheProduct = hashOps.entries(key);

		if (!hashCacheProduct.isEmpty()) {
			cacheHitCount.incrementAndGet(); // Cache Hit 카운트 증가
			return objectMapper.convertValue(hashCacheProduct, GetProductInfoResponse.class);
		}

		cacheMissCount.incrementAndGet();  // Cache Miss 카운터 증가
		dbQueryCount.incrementAndGet();

		Product product = productRepository.findById(productId).orElseThrow(
			() -> new CustomException(ErrorCode.PRODUCT_NOT_FOUND)
		);

		GetProductInfoResponse response = GetProductInfoResponse.from(product);

		Map<String, String> hashMap = objectMapper.convertValue(
			response,
			new TypeReference<Map<String, String>>() {}
		);

		hashOps.putAll(key, hashMap);
		stringRedisTemplate.expire(key, Duration.ofMinutes(10));

		return response;
	}

	private List<GetProductRankingResponse> toResponses(Set<ZSetOperations.TypedTuple<String>> result) {

		if (result == null || result.isEmpty()) {
			return Collections.emptyList();
		}

		List<ZSetOperations.TypedTuple<String>> tuples = result.stream()
			.filter(t -> t.getValue() != null && t.getScore() != null)
			.toList();

		List<Long> productIds = tuples.stream()
			.map(t -> Long.parseLong(t.getValue().split(":", 2)[0]))
			.toList();


		Map<Long, String> thumbnailMap = findThumbnailUrls(productIds);


		return tuples.stream()
			.map(t -> {
				String[] parts = t.getValue().split(":", 2);
				Long id = Long.parseLong(parts[0]);
				String title = parts[1];
				return new GetProductRankingResponse(
					id,
					title,
					thumbnailMap.get(id),
					t.getScore()
				);
			})
			.toList();
	}

	private Map<Long, String> findThumbnailUrls(List<Long> productIds) {

		HashOperations<String, String, String> hashOps = stringRedisTemplate.opsForHash();
		Map<Long, String> thumbnailMap = new HashMap<>();
		List<Long> missedIds = new ArrayList<>();

		for (Long id : productIds) {
			String cached = hashOps.get("product:info:" + id, "thumbnailUrl");
			if (cached != null) {
				thumbnailMap.put(id, cached);
			} else {
				missedIds.add(id);
			}
		}

		if (!missedIds.isEmpty()) {
			productRepository.findAllById(missedIds)
				.forEach(p -> thumbnailMap.put(p.getId(), p.getThumbnailUrl()));
		}

		return thumbnailMap;
	}

}
