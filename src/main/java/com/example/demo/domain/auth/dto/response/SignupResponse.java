package com.example.demo.domain.auth.dto.response;

import com.example.demo.domain.user.entity.User;

public record SignupResponse(
	Long userId,
	String email,
	String name,
	String phoneNumber,
	String role
) {
	public static SignupResponse from(User user){
		return new SignupResponse(
			user.getId(),
			user.getEmail(),
			user.getName(),
			user.getPhoneNumber(),
			user.getRole().name()
		);
	}
}
