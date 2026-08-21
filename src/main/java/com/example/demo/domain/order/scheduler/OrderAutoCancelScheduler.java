package com.example.demo.domain.order.scheduler;

import com.example.demo.domain.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderAutoCancelScheduler {

    private final OrderService orderService;

    /**
     * 주문 생성 후 1시간 동안 결제되지 않은 ORDERED 상태 주문을 자동 취소한다.
     * 스케줄러 자체에는 @Transactional을 걸지 않고, 주문 단건별 별도 트랜잭션으로 처리한다.
     */
    @Scheduled(fixedDelay = 5 * 1000)
    public void cancelExpiredOrders() {
        long startMs = System.currentTimeMillis();
        LocalDateTime expiredBefore = LocalDateTime.now().minusHours(1);

        List<Long> targetIds = orderService.findExpiredOrderIds(expiredBefore);

        int success = 0, fail = 0;
        for (Long orderId : targetIds) {
            try {
                orderService.cancelExpiredOrder(orderId);
                success++;
            } catch (Exception e) {
                fail++;
                log.warn("[AutoCancel] 실패 - orderId={}, cause={}", orderId, e.getMessage());
            }
        }

        long elapsed = System.currentTimeMillis() - startMs;
        log.info("[AutoCancel] 종료 - 대상={}, 성공={}, 실패={}, 소요시간={}ms, thread={}",
                targetIds.size(), success, fail, elapsed, Thread.currentThread().getName());
    }
}
