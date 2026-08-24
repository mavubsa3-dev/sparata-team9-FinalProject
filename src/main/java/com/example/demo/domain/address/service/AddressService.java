package com.example.demo.domain.address.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.common.exception.CustomException;
import com.example.demo.common.exception.ErrorCode;
import com.example.demo.domain.address.dto.request.CreateAddressRequest;
import com.example.demo.domain.address.dto.request.UpdateAddressRequest;
import com.example.demo.domain.address.dto.response.CreateAddressResponse;
import com.example.demo.domain.address.dto.response.DeleteAddressResponse;
import com.example.demo.domain.address.dto.response.GetAddressInfoResponse;
import com.example.demo.domain.address.dto.response.UpdateAddressResponse;
import com.example.demo.domain.address.entity.Address;
import com.example.demo.domain.address.repository.AddressRepository;
import com.example.demo.domain.user.entity.User;
import com.example.demo.domain.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
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
	public UpdateAddressResponse updateAddress(UpdateAddressRequest request, Long userId, Long addressId) {
		Address address = addressRepository.findByIdAndUserId(addressId, userId).orElseThrow(
			() -> new CustomException(ErrorCode.ADDRESS_NOT_FOUND)
		);

		if (request.alias() != null) address.updateAlias(request.alias());
		if (request.name() != null) address.updateName(request.name());
		if (request.phoneNumber() != null) address.updatePhoneNumber(request.phoneNumber());
		if (request.zipCode() != null) address.updateZipCode(request.zipCode());
		if (request.basicAddress() != null) address.updateBasicAddress(request.basicAddress());
		if (request.detailAddress() != null)address.updateDetailAddress(request.detailAddress());
		if (request.isDefault()){
			addressRepository.findByUserIdAndIsDefaultTrue(userId).ifPresent(
				existing -> {
					if (!existing.getId().equals(addressId)){
						existing.unsetDefault();
					}
				}
			);
			address.setDefault();
		}

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

	@Transactional
	public DeleteAddressResponse deleteAddress(Long userId, Long addressId){
		User user = userRepository.findById(userId).orElseThrow(
			() -> new CustomException(ErrorCode.USER_NOT_FOUND)
		);

		Address address = addressRepository.findById(addressId).orElseThrow(
			() -> new CustomException(ErrorCode.ADDRESS_NOT_FOUND)
		);

		addressRepository.delete(address);

		log.info("[주소 삭제] 사용자 : {}, 삭제한 주소 ID : {} ", user.getName(), address.getId());

		return DeleteAddressResponse.from(address);
	}
}
