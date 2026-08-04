package com.example.demo.domain.notification.dto.response;

import com.example.demo.domain.notification.entity.Notifications;

public record SendEmailMessageResponse(
	String to
) {
	public static SendEmailMessageResponse from(Notifications notifications){
		return new SendEmailMessageResponse(
			notifications.getUser().getName() + " 사용자에게 메일 전송"
		);
	}
}
