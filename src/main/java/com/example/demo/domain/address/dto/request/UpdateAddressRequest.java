package com.example.demo.domain.address.dto.request;

public record UpdateAddressRequest(
	String alias,
	String name,
	String phoneNumber,
	String zipCode,
	String basicAddress,
	String detailAddress
) {
}
