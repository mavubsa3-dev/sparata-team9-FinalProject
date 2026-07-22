package com.example.demo.common.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.example.demo.common.response.ErrorResponse;

@RestControllerAdvice
public class GlobalExceptionHandler {

	// 서비스 계층에서 발생한 비즈니스적 예외 처리
	@ExceptionHandler(CustomException.class)
	public ResponseEntity<ErrorResponse> handleCustomException(CustomException e) {
		ErrorCode errorCode = e.getErrorCode();

		ErrorResponse errorResponse = new ErrorResponse(
			errorCode.getCode(),
			errorCode.getMessage()
		);

		return ResponseEntity
			.status(errorCode.getStatus())
			.body(errorResponse);

	}

}
