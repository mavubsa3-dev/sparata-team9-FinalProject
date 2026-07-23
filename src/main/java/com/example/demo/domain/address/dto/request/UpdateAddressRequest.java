package com.example.demo.domain.address.dto.request;

import jakarta.validation.constraints.Pattern;

public record UpdateAddressRequest(
	String alias,
	String name,

	@Pattern(regexp = "^01(?:0|1|[6-9])[.-]?(\\d{3}|\\d{4})[.-]?(\\d{4})$", message = "올바른 휴대폰 번호 형식이 아닙니다.")
	String phoneNumber,

	@Pattern(regexp = "^\\d{5}$", message = "우편번호는 5자리 숫자여야 합니다.")
	String zipCode,

	String basicAddress,
	String detailAddress
) {
}
