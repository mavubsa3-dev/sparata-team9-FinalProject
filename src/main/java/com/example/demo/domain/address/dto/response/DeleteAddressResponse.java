package com.example.demo.domain.address.dto.response;

import com.example.demo.domain.address.entity.Address;

public record DeleteAddressResponse(
	Long addressId
) {
	public static DeleteAddressResponse from(Address address){
		return new DeleteAddressResponse(
			address.getId()
		);
	}
}
