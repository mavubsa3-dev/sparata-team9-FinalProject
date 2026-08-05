package com.example.demo.common.config.kafka.consumer;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.CommonErrorHandler;
import org.springframework.kafka.listener.DeadLetterPublishingRecoverer;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.kafka.support.serializer.JacksonJsonDeserializer;
import org.springframework.util.backoff.FixedBackOff;

import com.example.demo.common.entity.kafka.event.PaymentCompletedEvent;

@Configuration
public class KafkaConsumerConfig {

	@Value("${spring.kafka.bootstrap-servers}")
	private String bootStrapServers;

	public Map<String, Object> baseConsumerProps(String groupId){
		Map<String , Object> props = new HashMap<>();

		props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootStrapServers);
		props.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);

		props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
		props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JacksonJsonDeserializer.class);

		props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

		return props;
	}

	private ConsumerFactory<String, PaymentCompletedEvent> buildConsumerFactory(String groupId){
		JacksonJsonDeserializer<PaymentCompletedEvent> deserializer = new JacksonJsonDeserializer<>(
			PaymentCompletedEvent.class);

		deserializer.addTrustedPackages("*");

		return new DefaultKafkaConsumerFactory<>(
			baseConsumerProps(groupId),
			new StringDeserializer(),
			deserializer
		);
	}

	@Bean
	public ConsumerFactory<String, PaymentCompletedEvent> paymentHistoryConsumerFactory(){
		return buildConsumerFactory("payment-history");
	}

	@Bean
	public ConcurrentKafkaListenerContainerFactory<String, PaymentCompletedEvent> paymentCompletedKafkaListenerContainerFactory(){
		ConcurrentKafkaListenerContainerFactory<String, PaymentCompletedEvent> factory = new ConcurrentKafkaListenerContainerFactory<>();
		factory.setConsumerFactory(paymentHistoryConsumerFactory());

		return factory;
	}

	@Bean
	public ConsumerFactory<String ,String> errorConsumerFactory(){

		Map<String, Object> props = new HashMap<>();

		props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootStrapServers);
		props.put(ConsumerConfig.GROUP_ID_CONFIG, "kafka-error");

		props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
		props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);

		props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

		return new DefaultKafkaConsumerFactory<>(props);
	}

	@Bean
	public ConcurrentKafkaListenerContainerFactory<String ,String> errorKafkaListenerContainerFactory(
		CommonErrorHandler KafkaErrorHandler
	){

		ConcurrentKafkaListenerContainerFactory<String ,String> factory = new ConcurrentKafkaListenerContainerFactory<>();
		factory.setConsumerFactory(errorConsumerFactory());

		// 에러 핸들러 등록
		factory.setCommonErrorHandler(KafkaErrorHandler);

		return factory;
	}

	// kafka에러 핸들러
	@Bean
	public CommonErrorHandler KafkaErrorHandler(KafkaTemplate<String, String> stringKafkaTemplate){

		// 실패한 레코드 DLT 토픽으로 보냄
		DeadLetterPublishingRecoverer recoverer = new DeadLetterPublishingRecoverer(stringKafkaTemplate);

		// 1초 간격, 5번
		FixedBackOff backOff = new FixedBackOff(1000L, 5);

		return new DefaultErrorHandler(recoverer, backOff);
	}

}
