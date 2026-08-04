package com.example.demo.domain.notification.dto.request;

public record SendEmailMessageRequest(

	// 받는 사람
	String to,

	// 제목
	String subject,

	// 메세지
	String message
) {

}
