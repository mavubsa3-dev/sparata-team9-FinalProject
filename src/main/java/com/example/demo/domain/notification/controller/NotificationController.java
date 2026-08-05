package com.example.demo.domain.notification.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.domain.notification.dto.response.GetNotificationResponse;
import com.example.demo.domain.notification.dto.response.UpdateNotificationResponse;
import com.example.demo.domain.notification.service.NotificationService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/notifications")
public class NotificationController {

	private final NotificationService notificationService;

	@GetMapping
	public ResponseEntity<List<GetNotificationResponse>> getNotifications(@AuthenticationPrincipal Long userId){
		return ResponseEntity.status(HttpStatus.OK).body(notificationService.getNotifications(userId));
	}

	@GetMapping("/unread")
	public ResponseEntity<List<GetNotificationResponse>> getNotificationsUnread(@AuthenticationPrincipal Long userId){
		return ResponseEntity.status(HttpStatus.OK).body(notificationService.getUnreadNotifications(userId));
	}

	@PatchMapping("/{notificationId}/read")
	public ResponseEntity<UpdateNotificationResponse> updateNotificationIsRead(@AuthenticationPrincipal Long userId,
		@PathVariable Long notificationId){
		return ResponseEntity.status(HttpStatus.OK).body(notificationService.updateNotificationsIsRead(userId, notificationId));
	}


}
