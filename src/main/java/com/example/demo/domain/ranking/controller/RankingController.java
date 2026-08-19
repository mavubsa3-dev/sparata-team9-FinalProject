package com.example.demo.domain.ranking.controller;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.common.config.kafka.event.OrderItemInfo;
import com.example.demo.common.config.kafka.event.PaymentCompletedEvent;
import com.example.demo.domain.payment.producer.PaymentProducer;
import com.example.demo.domain.ranking.dto.response.GetProductInfoResponse;
import com.example.demo.domain.ranking.dto.response.GetProductRankingResponse;
import com.example.demo.domain.ranking.service.RankingService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/products/ranking")
public class RankingController {

	private final RankingService rankingService;
	private final PaymentProducer paymentProducer;

	// 테스트용
	private static final AtomicLong orderIdSequence = new AtomicLong(System.currentTimeMillis());

	@GetMapping
	public ResponseEntity<List<GetProductRankingResponse>> getProductRanking(@RequestParam(defaultValue = "10") int count ){
		return ResponseEntity.status(HttpStatus.OK).body(rankingService.findProductTopNInToday(count));
	}

	@GetMapping("/week")
	public ResponseEntity<List<GetProductRankingResponse>> getProductRankingIn7Days(@RequestParam(defaultValue = "10") int count ){
		return ResponseEntity.status(HttpStatus.OK).body(rankingService.findProductTopNInLast7Days(count));
	}

	@GetMapping("/{productId}")
	public ResponseEntity<GetProductInfoResponse> getProductInRanking(@PathVariable Long productId){
		return ResponseEntity.status(HttpStatus.OK).body(rankingService.getProductInRanking(productId));
	}

	@GetMapping("/week/{productId}")
	public ResponseEntity<GetProductInfoResponse> getProductInWeekRanking(@PathVariable Long productId){
		return ResponseEntity.status(HttpStatus.OK).body(rankingService.getProductInWeekRanking(productId));
	}

	@GetMapping("/cache/stats")
	public Map<String, Object> getCacheStats() {
		return rankingService.getCacheStats();
	}

	@PostMapping("/test/publish-payment-event")
	public void publishTestEvent(@RequestParam int count,
		@RequestParam(defaultValue = "false") boolean spreadUser) {
		for (int i = 0; i < count; i++) {
			long userId = spreadUser ? (i % 7) + 1 : 12L;
			long orderId = orderIdSequence.getAndIncrement();

			PaymentCompletedEvent event = PaymentCompletedEvent.builder()
				.paymentId(orderId)
				.userId(userId)
				.orderId(orderId)
				.orderNumber("TEST-ORDER-" + orderId)
				.totalAmount(10000L)
				.address("테스트 주소")
				.orderItems(List.of(new OrderItemInfo(1L, "테스트상품", 1)))
				.completedAt(LocalDateTime.now())
				.build();

			paymentProducer.send(event);
		}
	}
}
