package com.example.demo.domain.user.dto.response;

import com.example.demo.domain.user.entity.User;

public record UpdateUserResponse(
	String email,
	String name,
	String phoneNumber
) {
	public static UpdateUserResponse from(User user){
		return new UpdateUserResponse(
			user.getEmail(),
			user.getName(),
			user.getPhoneNumber()
		);
	}
}
