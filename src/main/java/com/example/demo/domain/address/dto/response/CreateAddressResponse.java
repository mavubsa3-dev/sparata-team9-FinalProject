package com.example.demo.domain.address.dto.response;

import com.example.demo.domain.address.entity.Address;
import com.example.demo.domain.user.entity.User;

public record CreateAddressResponse(
	Long id,
	String alias,
	String name,
	String phoneNumber,
	String zipCode,
	String basicAddress,
	String detailAddress
) {
	public static CreateAddressResponse from(Address address){
		return new CreateAddressResponse(
			address.getId(),
			address.getAlias(),
			address.getName(),
			address.getPhoneNumber(),
			address.getZipCode(),
			address.getBasicAddress(),
			address.getDetailAddress() == null ? "" : address.getDetailAddress()
		);
	}
}
