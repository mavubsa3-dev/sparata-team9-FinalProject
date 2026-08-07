package com.example.demo.domain.notification.dto.response;

import java.time.LocalDateTime;

import com.example.demo.domain.notification.entity.Notifications;

public record GetNotificationResponse(
	Long notificationId,
	String title,
	String message,
	boolean isRead,
	LocalDateTime createdAt,
	LocalDateTime updateAt
) {
	public static GetNotificationResponse from(Notifications notifications){
		return new GetNotificationResponse(
			notifications.getId(),
			notifications.getTitle(),
			notifications.getMessage(),
			notifications.isRead(),
			notifications.getCreatedAt(),
			notifications.getUpdatedAt()
		);
	}
}
