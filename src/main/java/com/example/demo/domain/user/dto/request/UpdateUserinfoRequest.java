package com.example.demo.domain.user.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;

public record UpdateUserinfoRequest(

	@Email(message = "이메일 형식이 올바르지 않습니다.")
	String email,

	String name,

	@Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,15}$",
		message = "비밀번호는 8~15자의 영문, 숫자, 특수문자를 포함해야 합니다.")
	String password,

	@Pattern(regexp = "^01(?:0|1|[6-9])[.-]?(\\d{3}|\\d{4})[.-]?(\\d{4})$", message = "올바른 휴대폰 번호 형식이 아닙니다.")
	String phoneNumber
) {
}
