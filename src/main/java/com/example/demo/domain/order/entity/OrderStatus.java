package com.example.demo.domain.order.entity;

public enum OrderStatus {
    ORDERED,   // 주문 생성 / 결제 대기
    PAID,      // 결제 완료
    COMPLETED, // 주문 처리 최종 완료
    CANCELED   // 주문 취소
}
