package com.example.demo.domain.payment.producer;

import static com.example.demo.common.config.kafka.topic.KafkaTopic.*;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.example.demo.common.config.kafka.event.PaymentCompletedEvent;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PaymentProducer {

	private final KafkaTemplate<String, PaymentCompletedEvent> paymentCompletedEventKafkaTemplate;

	public void send(PaymentCompletedEvent event){
		String key = event.getOrderNumber();

		paymentCompletedEventKafkaTemplate.send(TOPIC_PAYMENT_COMPLETED, key, event);
	}
}
