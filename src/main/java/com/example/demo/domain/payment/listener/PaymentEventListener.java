package com.example.demo.domain.payment.listener;

import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import com.example.demo.common.config.kafka.event.PaymentCompletedEvent;
import com.example.demo.domain.payment.producer.PaymentProducer;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class PaymentEventListener {

	private final PaymentProducer paymentProducer;

	@TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
	public void handlePaymentCompleted(PaymentCompletedEvent event){
		paymentProducer.send(event);
	}
}
