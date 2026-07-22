package com.example.demo.domain.user.dto.response;

public record GetUserInfo(
	String email,
	String name,
	String phoneNumber,
	String zip_Code,
	String basicAddress,
	String detailAddress
) {
}
