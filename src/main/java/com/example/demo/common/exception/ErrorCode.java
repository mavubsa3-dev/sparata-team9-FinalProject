package com.example.demo.common.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public enum ErrorCode {

	// User
	USER_NOT_FOUND(HttpStatus.NOT_FOUND, "USER_NOT_FOUND", "회원을 찾을 수 없습니다."),
	ADDRESS_NOT_FOUND(HttpStatus.NOT_FOUND, "ADDRESS_NOT_FOUND", "등록된 배송지를 찾을 수 없습니다."),
	ADDRESS_ACCESS_DENIED(HttpStatus.FORBIDDEN, "ADDRESS_ACCESS_DENIED", "해당 배송지에 접근할 권한이 없습니다."), // TODO: 임시 추가, 팀원 PR 병합 시 정리

	// Product
	PRODUCT_NOT_FOUND(HttpStatus.NOT_FOUND, "PRODUCT_NOT_FOUND", "상품을 찾을 수 없습니다."),
	PRODUCT_NOT_ON_SALE(HttpStatus.BAD_REQUEST, "PRODUCT_NOT_ON_SALE", "판매 중인 상품이 아닙니다."), // TODO: 임시 추가, 팀원 PR 병합 시 정리

	// Cart
	CART_ITEM_NOT_FOUND(HttpStatus.NOT_FOUND, "CART_ITEM_NOT_FOUND", "장바구니 상품을 찾을 수 없습니다."), // TODO: 임시 추가, 팀원 PR 병합 시 정리
	CART_ITEM_ACCESS_DENIED(HttpStatus.FORBIDDEN, "CART_ITEM_ACCESS_DENIED", "해당 장바구니 상품에 접근할 권한이 없습니다."), // TODO: 임시 추가, 팀원 PR 병합 시 정리

	// Order
	ORDER_STOCK_SHORTAGE(HttpStatus.BAD_REQUEST, "ORDER_STOCK_SHORTAGE", "재고가 부족합니다.");

	private final HttpStatus httpStatus;
	private final String code;
	private final String message;

	ErrorCode(HttpStatus httpStatus, String code, String message) {
		this.httpStatus = httpStatus;
		this.code = code;
		this.message = message;
	}

	public int getStatus() {
		return httpStatus.value();
	}
}
