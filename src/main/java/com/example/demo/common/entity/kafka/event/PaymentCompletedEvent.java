package com.example.demo.common.entity.kafka.event;

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
	private String orderNumber;
	private Map<String, Integer> orderItem;
	private Long totalAmount;
	private Long balance;
	private String paidAt;
}
