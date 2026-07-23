package com.example.demo.domain.address.dto.request;

import com.example.demo.domain.user.entity.User;

public record CreateAddressRequest(
	String alias,
	String name,
	String phoneNumber,
	String zipCode,
	String basicAddress,
	String detailAddress
) {
}
