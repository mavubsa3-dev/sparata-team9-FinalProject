package com.example.demo.domain.payment.controller;

import com.example.demo.domain.payment.dto.response.GetSettlementResponse;
import com.example.demo.domain.payment.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/settlements")
public class AdminSettlementController {

    private final PaymentService paymentService;

    @GetMapping
    public ResponseEntity<List<GetSettlementResponse>> getSettlements() {
        return ResponseEntity.ok(paymentService.getSettlements());
    }

    @GetMapping("/{settlementDate}")
    public ResponseEntity<GetSettlementResponse> getSettlement(
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate settlementDate
    ) {
        return ResponseEntity.ok(paymentService.getSettlement(settlementDate));
    }

    /**
     * 특정 날짜의 정산을 즉시 수동 재집계한다.
     * 자정 배치를 기다리지 않고 테스트하거나, 누락된 날짜를 다시 채울 때 사용한다.
     */
    @PostMapping("/{settlementDate}/aggregate")
    public ResponseEntity<GetSettlementResponse> aggregate(
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate settlementDate
    ) {
        return ResponseEntity.ok(paymentService.aggregateSettlementForAdmin(settlementDate));
    }
}