package com.example.demo.domain.user.dto.response;

import com.example.demo.domain.address.entity.Address;
import com.example.demo.domain.user.entity.User;

public record GetUserInfoResponse(
	String email,
	String name,
	String phoneNumber
) {
	public static GetUserInfoResponse from(User user){
		return new GetUserInfoResponse(
			user.getEmail(),
			user.getName(),
			user.getPhoneNumber()
		);
	}
}
