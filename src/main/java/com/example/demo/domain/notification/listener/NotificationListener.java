package com.example.demo.domain.notification.listener;

import static com.example.demo.common.config.kafka.topic.KafkaTopic.*;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import com.example.demo.common.config.kafka.event.PaymentCompletedEvent;
import com.example.demo.domain.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class NotificationListener {

	private final NotificationService notificationService;

	@KafkaListener(
		topics = TOPIC_PAYMENT_COMPLETED,
		groupId = "payment-history",
		containerFactory = "paymentCompletedKafkaListenerContainerFactory"
	) public void consumer(PaymentCompletedEvent event){

		notificationService.saveAndSend(event);
	}

	@KafkaListener(
		topics = TOPIC_PAYMENT_COMPLETED + "-dlt",
		groupId = "payment-dlt",
		containerFactory = "paymentCompletedKafkaListenerContainerFactory"
	) public void dltConsumer(PaymentCompletedEvent event){

		log.error("[재시도 실패 DLT 전송] paymentId : {} ", event.paymentId());
	}
}
