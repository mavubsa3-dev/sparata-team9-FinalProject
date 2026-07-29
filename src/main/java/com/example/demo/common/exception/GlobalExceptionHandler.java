package com.example.demo.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
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

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ErrorResponse> handleValidationException(MethodArgumentNotValidException e) {

		BindingResult bindingResult = e.getBindingResult();

		String errorMessage = bindingResult.getFieldError().getDefaultMessage();

		ErrorResponse errorResponse = new ErrorResponse(
			"VALIDATION_FAILED",
			errorMessage
		);

		return ResponseEntity
			.status(HttpStatus.BAD_REQUEST)
			.body(errorResponse);
	}

}
