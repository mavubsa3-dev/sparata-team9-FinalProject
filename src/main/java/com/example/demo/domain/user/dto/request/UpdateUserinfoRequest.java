package com.example.demo.domain.user.dto.request;

public record UpdateUserinfoRequest(
	String email,
	String name,
	String phoneNumber
) {
}
