package com.example.demo.domain.payment.scheduler;

import com.example.demo.domain.payment.entity.Settlement;
import com.example.demo.domain.payment.service.PaymentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.ZoneId;

@Slf4j
@Component
@RequiredArgsConstructor
public class SettlementScheduler {

    private static final ZoneId KST = ZoneId.of("Asia/Seoul");

    private final PaymentService paymentService;

    /**
     * 매일 00:00(KST)에 어제 하루치 매출을 집계한다.
     * cron 필드: 초 분 시 일 월 요일
     */
    @Scheduled(cron = "0 0 0 * * *", zone = "Asia/Seoul")
    public void aggregateYesterdaySettlement() {
        long startMs = System.currentTimeMillis();
        LocalDate targetDate = LocalDate.now(KST).minusDays(1);

        try {
            Settlement settlement = paymentService.aggregateSettlement(targetDate);
            long elapsed = System.currentTimeMillis() - startMs;
            log.info("[Settlement] 완료 - date={}, totalAmount={}, orderCount={}, 소요시간={}ms, thread={}",
                    settlement.getSettlementDate(), settlement.getTotalAmount(),
                    settlement.getOrderCount(), elapsed, Thread.currentThread().getName());
        } catch (Exception e) {
            log.error("[Settlement] 실패 - date={}, cause={}", targetDate, e.getMessage(), e);
        }
    }
}
