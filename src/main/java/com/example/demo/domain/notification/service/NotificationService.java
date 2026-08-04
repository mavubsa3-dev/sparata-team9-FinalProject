package com.example.demo.domain.notification.service;

import org.springframework.stereotype.Service;

import com.example.demo.common.config.kafka.event.PaymentCompletedEvent;
import com.example.demo.common.exception.CustomException;
import com.example.demo.common.exception.ErrorCode;
import com.example.demo.domain.notification.Repository.NotificationRepository;
import com.example.demo.domain.notification.dto.request.SendEmailMessageRequest;
import com.example.demo.domain.notification.entity.Notifications;
import com.example.demo.domain.user.entity.User;
import com.example.demo.domain.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class NotificationService {

	private final NotificationRepository notificationRepository;
	private final EmailService emailService;
	private final UserRepository userRepository;

	public void saveAndSend(PaymentCompletedEvent event){
		User user = userRepository.findById(event.getUserId()).orElseThrow(
			() -> new CustomException(ErrorCode.USER_NOT_FOUND)
		);

		String title = "[결제 완료] 주문 번호 " + event.getOrderNumber();

		String message = String.format(
			"<p><b>주문번호:</b> %s</p>" +
				"<p><b>결제금액:</b> %d원</p>" +
				"<p><b>결제일시:</b> %s</p>",
			event.getOrderNumber(),
			event.getTotalAmount(),
			event.getPaidAt()
		);

		notificationRepository.save(new Notifications(user, title, message));

		emailService.send(new SendEmailMessageRequest(
			user.getEmail(),
			title,
			message
		));
	}
}
