package com.example.demo.domain.notification.service;


import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.common.config.kafka.event.OrderItemInfo;
import com.example.demo.common.config.kafka.event.PaymentCompletedEvent;
import com.example.demo.common.exception.CustomException;
import com.example.demo.common.exception.ErrorCode;
import com.example.demo.domain.notification.Repository.NotificationRepository;
import com.example.demo.domain.notification.dto.request.SendEmailMessageRequest;
import com.example.demo.domain.notification.dto.response.GetNotificationResponse;
import com.example.demo.domain.notification.dto.response.UpdateNotificationResponse;
import com.example.demo.domain.notification.entity.Notifications;
import com.example.demo.domain.user.entity.User;
import com.example.demo.domain.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationService {

	private final NotificationRepository notificationRepository;
	private final EmailService emailService;
	private final UserRepository userRepository;

	@Transactional
	public void saveAndSend(PaymentCompletedEvent event){
		User user = userRepository.findById(event.userId()).orElseThrow(
			() -> new CustomException(ErrorCode.USER_NOT_FOUND)
		);

		String title = "[결제 완료] 주문 번호 " + event.orderId();

		saveNotification(user, title, event);
		sendEmailNotification(user, event, title);
	}

	@Transactional(readOnly = true)
	public List<GetNotificationResponse> getNotifications(Long userId){
		User user = userRepository.findById(userId).orElseThrow(
			() -> new CustomException(ErrorCode.USER_NOT_FOUND)
		);

		log.info("[알림 조회] 조회한 사용자 {} ", user.getName());

		return notificationRepository.findAllByUserIdOrderByCreatedAtDesc(userId).stream()
			.map(GetNotificationResponse::from)
			.toList();
	}

	@Transactional(readOnly = true)
	public List<GetNotificationResponse> getUnreadNotifications(Long userId){
		User user = userRepository.findById(userId).orElseThrow(
			() -> new CustomException(ErrorCode.USER_NOT_FOUND)
		);

		log.info("[미열람 알람 조회] 조회한 사용자 {} ", user.getName());

		return notificationRepository.findByUserIdAndIsReadFalseOrderByCreatedAtDesc(userId).stream()
			.map(GetNotificationResponse::from)
			.toList();
	}

	@Transactional
	public UpdateNotificationResponse updateNotificationsIsRead(Long userId, Long notificationId){
		Notifications notifications = notificationRepository.findByIdAndUserId(notificationId, userId).orElseThrow(
			() -> new CustomException(ErrorCode.NOTIFICATION_NOT_FOUND)
		);

		notifications.markAsRead();

		return UpdateNotificationResponse.from(notifications);
	}

	private void saveNotification(User user, String title, PaymentCompletedEvent event){

		String dbProductList = formatOrderItems(event.orderItems(), "- ", "%s %d개");

		String dbMessage = String.format(
			"주문번호: %s\n" +
				"주문 상품:\n%s\n" +
				"결제금액: %d원\n" +
				"결제일시: %s",
			event.orderId(),
			dbProductList,
			event.totalAmount(),
			event.completedAt()
		);

		notificationRepository.save(new Notifications(user, title, dbMessage));
	}

	private void sendEmailNotification(User user, PaymentCompletedEvent event, String title){

		String htmlProductList = event.orderItems().stream()
			.map(item -> String.format("<p>- %s x %d개</p>", item.productName(), item.quantity()))
			.collect(Collectors.joining(""));

		String message = String.format(
			"<h3>결제가 정상적으로 완료되었습니다.</h3>" +
				"<p><b>주문번호:</b> %s</p>" +
				"<p><b>주문 상품:</b></p>" +
				"%s" +
				"<p><b>결제금액:</b> %d원</p>" +
				"<p><b>결제일시:</b> %s</p>",
			event.orderId(),
			htmlProductList,
			event.totalAmount(),
			event.completedAt()
		);

		emailService.send(new SendEmailMessageRequest(
			user.getEmail(),
			title,
			message
		));
	}

	private String formatOrderItems(List<OrderItemInfo> orderItems, String prefix, String format) {
		return orderItems.stream()
			.map(item -> String.format(format, item.productName(), item.quantity()))
			.collect(Collectors.joining("\n" + prefix, prefix, ""));
	}
}
