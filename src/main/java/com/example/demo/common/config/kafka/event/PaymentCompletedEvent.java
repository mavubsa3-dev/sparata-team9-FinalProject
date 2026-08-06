package com.example.demo.common.config.kafka.event;

import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentCompletedEvent {

	private Long paymentId;
	private Long userId;
	private String userEmail;
	private String orderNumber;
	private Long totalAmount;
	private Map<String ,Integer> products;
	private String paidAt;

}
