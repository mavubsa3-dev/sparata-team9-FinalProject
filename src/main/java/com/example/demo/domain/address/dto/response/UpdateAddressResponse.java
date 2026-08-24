package com.example.demo.domain.address.dto.response;

import com.example.demo.domain.address.entity.Address;

public record UpdateAddressResponse(
	Long id,
	String alias,
	String name,
	String phoneNumber,
	String zipCode,
	String basicAddress,
	String detailAddress,
	boolean isDefault
) {
	public static UpdateAddressResponse from(Address address){
		return new UpdateAddressResponse(
			address.getId(),
			address.getAlias(),
			address.getName(),
			address.getPhoneNumber(),
			address.getZipCode(),
			address.getBasicAddress(),
			address.getDetailAddress() == null ? "" : address.getDetailAddress(),
			address.isDefault()
		);
	}
}
