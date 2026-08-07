package com.example.demo.domain.notification.dto.response;

import java.time.LocalDateTime;
import com.example.demo.domain.notification.entity.Notifications;

public record UpdateNotificationResponse(
	Long notificationId,
	String title,
	boolean isRead,
	LocalDateTime updateAt
) {
	public static UpdateNotificationResponse from(Notifications notifications){
		return new UpdateNotificationResponse(
			notifications.getId(),
			notifications.getTitle(),
			notifications.isRead(),
			notifications.getUpdatedAt()
		);
	}
}
