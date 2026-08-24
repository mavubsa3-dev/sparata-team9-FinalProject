// package com.example.demo.domain;
//
// import java.time.LocalDate;
// import java.util.ArrayList;
// import java.util.List;
//
// import org.springframework.boot.ApplicationArguments;
// import org.springframework.boot.ApplicationRunner;
// import org.springframework.context.annotation.Profile;
// import org.springframework.data.redis.core.StringRedisTemplate;
// import org.springframework.stereotype.Component;
//
// import com.example.demo.domain.category.entity.Category;
// import com.example.demo.domain.category.repository.CategoryRepository;
// import com.example.demo.domain.product.entity.Product;
// import com.example.demo.domain.product.repository.ProductRepository;
// import com.example.demo.domain.ranking.service.RankingService;
//
// import lombok.RequiredArgsConstructor;
// import lombok.extern.slf4j.Slf4j;
//
// @Slf4j
// @Component
// @Profile("local")
// @RequiredArgsConstructor
// public class dummyDate implements ApplicationRunner {
//
// 	private final ProductRepository productRepository;
// 	private final CategoryRepository categoryRepository;
// 	private final StringRedisTemplate stringRedisTemplate;
// 	private final RankingService rankingService;
//
// 	private static final String PRODUCT_RANKING_KEY = "product:ranking:";
// 	private static final int TOTAL_K6_PRODUCTS = 3000; // k6 부하 테스트용 총 상품 수
// 	private static final int RANKING_TOP_N = 100;      // 랭킹에 올릴 상위 상품 수
// 	private static final int BATCH_SIZE = 500;         // DB 저장 청크 단위
//
// 	@Override
// 	public void run(ApplicationArguments args) { // ★ @Transactional 제거!
// 		log.info("============== [2단계: k6 부하테스트용 대량 데이터 & Redis 초기화 시작] ==============");
//
// 		// 1. 기존 Redis 랭킹 키 초기화
// 		cleanUpRedis();
//
// 		// 2. data.sql로 들어간 1번 카테고리("전자기기") 가져오기
// 		Category defaultCategory = categoryRepository.findById(1L)
// 			.orElseThrow(() -> new IllegalStateException("1번 카테고리가 존재하지 않습니다. data.sql 실행 여부 및 application.yml 설정을 확인하세요."));
//
// 		long currentProductCount = productRepository.count();
//
// 		if (currentProductCount >= TOTAL_K6_PRODUCTS) {
// 			log.info(" - 이미 [{}]개 상품이 존재하여 더미 상품 생성을 건너뜁니다.", currentProductCount);
// 		} else {
// 			// 3. 9번부터 3,000번까지 Product 엔티티 생성 후 500개씩 나누어 저장 (커넥션 타임아웃 방지)
// 			List<Product> batchList = new ArrayList<>();
// 			for (int i = 9; i <= TOTAL_K6_PRODUCTS; i++) {
// 				Product product = new Product(
// 					defaultCategory,
// 					"k6_테스트상품_" + i,
// 					"k6 부하테스트 및 목록 페이징 검증용 설명 " + i,
// 					"url/dummy.jpg",
// 					10000L + (i * 100L),
// 					100
// 				);
// 				batchList.add(product);
//
// 				// 500개가 모일 때마다 저장 후 리스트 비우기
// 				if (batchList.size() >= BATCH_SIZE) {
// 					productRepository.saveAll(batchList);
// 					batchList.clear();
// 				}
// 			}
//
// 			// 남은 데이터 저장
// 			if (!batchList.isEmpty()) {
// 				productRepository.saveAll(batchList);
// 			}
// 		}
//
//
// 		// 전체 상품 목록 조회
// 		List<Product> allProducts = productRepository.findAll();
// 		log.info(" - MySQL 전체 상품 수: [{}]개 완성!", allProducts.size());
//
// 		// 4. 상위 100개 상품에 대해 7일 치 일간/주간 랭킹 점수 적재
// 		createDummyRankings(allProducts);
//
// 		log.info("============== [2단계: k6 부하테스트용 대량 데이터 & Redis 초기화 완료] ==============");
// 	}
//
// 	private void cleanUpRedis() {
// 		try {
// 			var redisKeys = stringRedisTemplate.keys(PRODUCT_RANKING_KEY + "*");
// 			if (redisKeys != null && !redisKeys.isEmpty()) {
// 				stringRedisTemplate.delete(redisKeys);
// 			}
// 			log.info(" - 기존 Redis 랭킹 키 삭제 완료");
// 		} catch (Exception e) {
// 			log.warn(" - Redis 서버 연결 상태 확인 필요: {}", e.getMessage());
// 		}
// 	}
//
// 	private void createDummyRankings(List<Product> products) {
// 		LocalDate today = LocalDate.now();
//
// 		for (int i = 0; i < Math.min(RANKING_TOP_N, products.size()); i++) {
// 			Product product = products.get(i);
// 			String productInfo = product.getId() + ":" + product.getName();
//
// 			int baseScore = (RANKING_TOP_N - i) * 10;
//
// 			// 1) 오늘 일간 랭킹
// 			rankingService.increaseScore(productInfo, baseScore);
//
// 			// 2) 최근 6일간 과거 랭킹
// 			for (int dayAgo = 1; dayAgo <= 6; dayAgo++) {
// 				LocalDate pastDate = today.minusDays(dayAgo);
// 				String pastKey = PRODUCT_RANKING_KEY + pastDate;
//
// 				stringRedisTemplate.opsForZSet().incrementScore(
// 					pastKey,
// 					productInfo,
// 					baseScore - (dayAgo * 2)
// 				);
// 			}
// 		}
// 		log.info(" - Redis 상위 [{}]개 상품 일간/주간(7일) 랭킹 점수 적재 완료!", RANKING_TOP_N);
// 	}
// }
