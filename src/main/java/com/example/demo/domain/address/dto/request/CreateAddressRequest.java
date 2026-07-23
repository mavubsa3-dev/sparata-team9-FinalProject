package com.example.demo.domain.address.dto.request;

import com.example.demo.domain.user.entity.User;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record CreateAddressRequest(
	@NotBlank(message = "수령지를 입력하세요")
	String alias,

	@NotBlank(message = "수령인 이름은 필수입니다.")
	String name,

	@NotBlank(message = "휴대폰 번호를 입력하세요")
	@Pattern(regexp = "^01(?:0|1|[6-9])[.-]?(\\d{3}|\\d{4})[.-]?(\\d{4})$", message = "올바른 휴대폰 번호 형식이 아닙니다.")
	String phoneNumber,

	@NotBlank(message = "우편번호를 입력해 주세요.")
	@Pattern(regexp = "^\\d{5}$", message = "우편번호는 5자리 숫자여야 합니다.")
	String zipCode,

	@NotBlank(message = "기본 주소를 입력해 주세요.")
	String basicAddress,
	String detailAddress
) {
}
