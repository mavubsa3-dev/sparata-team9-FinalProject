package com.example.demo.common.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public enum ErrorCode {

    // User
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "USER_NOT_FOUND", "회원을 찾을 수 없습니다."),
    ADDRESS_NOT_FOUND(HttpStatus.NOT_FOUND, "ADDRESS_NOT_FOUND", "등록된 배송지를 찾을 수 없습니다."),

    // Product
    PRODUCT_NOT_FOUND(HttpStatus.NOT_FOUND, "PRODUCT_NOT_FOUND", "상품을 찾을 수 없습니다."),

	// Cart
	CART_NOT_FOUND(HttpStatus.NOT_FOUND, "CART_NOT_FOUND", "장바구니를 찾을 수 없습니다."),
	INSUFFICIENT_STOCK(HttpStatus.BAD_REQUEST, "INSUFFICIENT_STOCK", "재고가 부족합니다."),
	CART_ITEM_NOT_FOUND(HttpStatus.NOT_FOUND, "CART_ITEM_NOT_FOUND", "장바구니 상품을 찾을 수 없습니다."),
	PRODUCT_NOT_PURCHASABLE(HttpStatus.BAD_REQUEST, "PRODUCT_NOT_PURCHASABLE", "구매할 수 없는 상품입니다."),
	DUPLICATE_CART_ITEM(HttpStatus.CONFLICT, "DUPLICATE_CART_ITEM", "이미 처리 중인 요청입니다. 다시 시도해주세요."),
	CART_ITEM_ACCESS_DENIED(HttpStatus.FORBIDDEN, "CART_ITEM_ACCESS_DENIED", "해당 장바구니 상품에 접근할 권한이 없습니다.");

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
