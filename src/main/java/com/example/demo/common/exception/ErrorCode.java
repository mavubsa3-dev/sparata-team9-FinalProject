package com.example.demo.common.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public enum ErrorCode {

	// 400 BAD_REQUEST
	INVALID_CREDENTIALS(HttpStatus.BAD_REQUEST, "INVALID_CREDENTIALS", "이메일 또는 비밀번호가 올바르지 않습니다."),
	INVALID_QUANTITY(HttpStatus.BAD_REQUEST, "INVALID_QUNATITY", "수량은 1 이상이어야 합니다"),
	PRODUCT_NOT_ON_SALE(HttpStatus.BAD_REQUEST, "PRODUCT_NOT_ON_SALE", "판매 중인 상품이 아닙니다."),
	PRODUCT_NOT_PURCHASABLE(HttpStatus.BAD_REQUEST, "PRODUCT_NOT_PURCHASABLE", "구매할 수 없는 상품입니다."),
	INSUFFICIENT_STOCK(HttpStatus.BAD_REQUEST, "INSUFFICIENT_STOCK", "재고가 부족합니다."),
	ORDER_CANNOT_CANCEL(HttpStatus.BAD_REQUEST, "ORDER_CANNOT_CANCEL", "결제 전 주문만 취소할 수 있습니다."),
	ORDER_NOT_PAYABLE(HttpStatus.BAD_REQUEST, "ORDER_NOT_PAYABLE", "결제할 수 없는 주문 상태입니다."),
	PAYMENT_CANNOT_CANCEL(HttpStatus.BAD_REQUEST, "PAYMENT_CANNOT_CANCEL", "이미 처리된 결제는 취소할 수 없습니다."),
	PAYMENT_AMOUNT_MISMATCH(HttpStatus.BAD_REQUEST, "PAYMENT_AMOUNT_MISMATCH", "결제 승인 금액이 일치하지 않습니다."),
	PAYMENT_NOT_APPROVABLE(HttpStatus.BAD_REQUEST, "PAYMENT_NOT_APPROVABLE", "승인할 수 없는 결제 상태입니다."),
	WEBHOOK_PAYLOAD_PARSE_FAILED(HttpStatus.BAD_REQUEST, "WEBHOOK_PAYLOAD_PARSE_FAILED", "결제 알림 내용을 읽을 수 없습니다."),
	WEBHOOK_PAYMENT_ID_FORMAT_INVALID(HttpStatus.BAD_REQUEST, "WEBHOOK_PAYMENT_ID_FORMAT_INVALID", "전달받은 결제 정보에서 어떤 주문의 결제인지 확인할 수 없습니다."),

	// 401 UNAUTHORIZED
	UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "UNAUTHORIZED", "로그인이 필요합니다."),
	WEBHOOK_SIGNATURE_INVALID(HttpStatus.UNAUTHORIZED, "WEBHOOK_SIGNATURE_INVALID", "결제 알림을 보낸 곳이 신뢰할 수 있는 곳인지 확인할 수 없습니다."),
	// 403 FORBIDDEN
	FORBIDDEN(HttpStatus.FORBIDDEN, "FORBIDDEN", "접근 권한이 없습니다"),
	ADDRESS_ACCESS_DENIED(HttpStatus.FORBIDDEN, "ADDRESS_ACCESS_DENIED", "해당 배송지에 접근할 권한이 없습니다."),
	CART_ITEM_ACCESS_DENIED(HttpStatus.FORBIDDEN, "CART_ITEM_ACCESS_DENIED", "해당 장바구니 상품에 접근할 권한이 없습니다."),
	ORDER_ACCESS_DENIED(HttpStatus.FORBIDDEN, "ORDER_ACCESS_DENIED", "해당 주문에 접근할 권한이 없습니다."),
	PAYMENT_ACCESS_DENIED(HttpStatus.FORBIDDEN, "PAYMENT_ACCESS_DENIED", "해당 결제에 접근할 권한이 없습니다."),

    // 404 NOT_FOUND
	USER_NOT_FOUND(HttpStatus.NOT_FOUND, "USER_NOT_FOUND", "회원을 찾을 수 없습니다."),
	ADMIN_NOT_FOUND(HttpStatus.NOT_FOUND, "ADMIN_NOT_FOUND", "관리자를 찾을 수 없습니다"),
	ADDRESS_NOT_FOUND(HttpStatus.NOT_FOUND, "ADDRESS_NOT_FOUND", "등록된 배송지를 찾을 수 없습니다."),
    PRODUCT_NOT_FOUND(HttpStatus.NOT_FOUND, "PRODUCT_NOT_FOUND", "상품을 찾을 수 없습니다."),
	CART_NOT_FOUND(HttpStatus.NOT_FOUND, "CART_NOT_FOUND", "장바구니를 찾을 수 없습니다."),
	CART_ITEM_NOT_FOUND(HttpStatus.NOT_FOUND, "CART_ITEM_NOT_FOUND", "장바구니 상품을 찾을 수 없습니다."),
	ORDER_NOT_FOUND(HttpStatus.NOT_FOUND, "ORDER_NOT_FOUND", "주문을 찾을 수 없습니다."),
	CATEGORY_NOT_FOUND(HttpStatus.NOT_FOUND, "CATEGORY_NOT_FOUND", "카테고리를 찾을 수 없습니다."),
	PAYMENT_NOT_FOUND(HttpStatus.NOT_FOUND, "PAYMENT_NOT_FOUND", "결제 내역을 찾을 수 없습니다."),
	NOTIFICATION_NOT_FOUND(HttpStatus.NOT_FOUND, "NOTIFICATION_NOT_FOUND", "알림을 찾을 수 없습니다."),
	PRODUCT_NOT_IN_RANKING(HttpStatus.NOT_FOUND, "PRODUCT_NOT_IN_RANKING", "해당 상품은 현재 랭킹에 존재하지 않습니다."),

	// 409 CONFLICT
	CATEGORY_HAS_PRODUCTS(HttpStatus.CONFLICT, "CATEGORY_HAS_PRODUCTS", "해당 카테고리에 속한 상품이 존재하여 삭제할 수 없습니다."),
	DUPLICATE_CART_ITEM(HttpStatus.CONFLICT, "DUPLICATE_CART_ITEM", "이미 처리 중인 요청입니다. 다시 시도해주세요."),
	EMAIL_DUPLICATE(HttpStatus.CONFLICT, "EMAIL_DUPLICATE", "이미 사용 중인 이메일입니다."),
	PAYMENT_ALREADY_EXISTS(HttpStatus.CONFLICT, "PAYMENT_ALREADY_EXISTS", "이미 결제가 생성된 주문입니다."),

	// 500 INTERNAL_SERVER_ERROR
	IMAGE_UPLOAD_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "IMAGE_UPLOAD_FAILED", "이미지 업로드에 실패했습니다.");

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
