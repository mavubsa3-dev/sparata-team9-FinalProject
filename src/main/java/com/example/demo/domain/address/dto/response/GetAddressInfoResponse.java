package com.example.demo.domain.address.dto.response;

import com.example.demo.domain.address.entity.Address;

public record GetAddressInfoResponse(
	String alias,
	String name,
	String phoneNumber,
	String zipCode,
	String basicAddress,
	String detailAddress
) {
	public static GetAddressInfoResponse from(Address address){
		return new GetAddressInfoResponse(
			address.getAlias(),
			address.getName(),
			address.getPhoneNumber(),
			address.getZipCode(),
			address.getBasicAddress(),
			address.getDetailAddress() == null ? "" : address.getDetailAddress()
		);
	}
}
