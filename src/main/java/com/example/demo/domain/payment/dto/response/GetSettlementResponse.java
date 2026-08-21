package com.example.demo.domain.payment.dto.response;

import com.example.demo.domain.payment.entity.Settlement;

import java.time.LocalDate;

public record GetSettlementResponse(
        Long settlementId,
        LocalDate settlementDate,
        Long totalAmount,
        Long orderCount
) {

    public static GetSettlementResponse from(Settlement settlement) {
        return new GetSettlementResponse(
                settlement.getId(),
                settlement.getSettlementDate(),
                settlement.getTotalAmount(),
                settlement.getOrderCount()
        );
    }
}