package com.example.demo.common.response;


public record ErrorResponse(
	String code,
	String message
) {
}
