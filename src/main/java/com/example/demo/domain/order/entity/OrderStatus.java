package com.example.demo.domain.order.entity;

public enum OrderStatus {
    ORDERED,   // 주문 접수 (결제 대기 중)
    COMPLETED, // 주문 처리 완료
    CANCELED   // 주문 취소
}
