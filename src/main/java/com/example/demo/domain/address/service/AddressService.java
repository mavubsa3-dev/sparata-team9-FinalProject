package com.example.demo.domain.address.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.common.exception.CustomException;
import com.example.demo.common.exception.ErrorCode;
import com.example.demo.domain.address.dto.request.CreateAddressRequest;
import com.example.demo.domain.address.dto.request.UpdateAddressRequest;
import com.example.demo.domain.address.dto.response.CreateAddressResponse;
import com.example.demo.domain.address.dto.response.GetAddressInfoResponse;
import com.example.demo.domain.address.dto.response.UpdateAddressResponse;
import com.example.demo.domain.address.entity.Address;
import com.example.demo.domain.address.repository.AddressRepository;
import com.example.demo.domain.user.entity.User;
import com.example.demo.domain.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AddressService {

	private final AddressRepository addressRepository;
	private final UserRepository userRepository;

	@Transactional(readOnly = true)
	public List<GetAddressInfoResponse> getAddressInfo(Long userId) {
		// 추후 페이징 처리 예정
		return addressRepository.findAllByUserId(userId).stream()
			.map(GetAddressInfoResponse::from)
			.toList();
	}

	@Transactional
	public UpdateAddressResponse UpdateAddress(UpdateAddressRequest request, Long userId, Long addressId) {
		Address address = addressRepository.findByIdAndUserId(addressId, userId).orElseThrow(
			() -> new CustomException(ErrorCode.ADDRESS_NOT_FOUND)
		);

		if (request.alias() != null) address.updateAlias(request.alias());
		if (request.name() != null) address.updateName(request.name());
		if (request.phoneNumber() != null) address.updatePhoneNumber(request.phoneNumber());
		if (request.zipCode() != null) address.updateZipCode(request.zipCode());
		if (request.basicAddress() != null) address.updateBasicAddress(request.basicAddress());
		if (request.detailAddress() != null)address.updateDetailAddress(request.detailAddress());

		return UpdateAddressResponse.from(address);
	}

	@Transactional
	public CreateAddressResponse createAddress(CreateAddressRequest request, Long userId) {
		User user = userRepository.findById(userId).orElseThrow(
			() -> new CustomException(ErrorCode.USER_NOT_FOUND)
		);

		Address address = new Address(
			user,
			request.alias(),
			request.name(),
			request.phoneNumber(),
			request.zipCode(),
			request.basicAddress(),
			request.detailAddress() == null ? "" : request.detailAddress()
		);

		Address savedAddress = addressRepository.save(address);
		return CreateAddressResponse.from(savedAddress);
	}
}
