package com.example.demo.domain.user.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.common.exception.CustomException;
import com.example.demo.common.exception.ErrorCode;
import com.example.demo.domain.address.entity.Address;
import com.example.demo.domain.address.repository.AddressRepository;
import com.example.demo.domain.user.dto.request.UpdateUserinfoRequest;
import com.example.demo.domain.user.dto.response.GetUserInfoResponse;
import com.example.demo.domain.user.dto.response.UpdateUserResponse;
import com.example.demo.domain.user.entity.User;
import com.example.demo.domain.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

	private final UserRepository userRepository;

	@Transactional(readOnly = true)
	public GetUserInfoResponse userInfo(Long userId) {
		User user = userRepository.findById(userId).orElseThrow(
			() -> new CustomException(ErrorCode.USER_NOT_FOUND)
		);
		return GetUserInfoResponse.from(user);
	}

	@Transactional
	public UpdateUserResponse updateUserInfo(UpdateUserinfoRequest request, Long userId) {
		User user = userRepository.findById(userId).orElseThrow(
			() -> new CustomException(ErrorCode.USER_NOT_FOUND)
		);

		// 회원 정보 업데이트
		if (request.email() != null) user.updateEmail(request.email());
		if (request.name() != null) user.updateName(request.name());
		if (request.phoneNumber() != null) user.updatePhoneNumber(request.phoneNumber());

		return UpdateUserResponse.from(user);
	}
}
