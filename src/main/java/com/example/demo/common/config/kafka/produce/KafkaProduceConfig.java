package com.example.demo.common.config.kafka.produce;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JacksonJsonSerializer;
import com.example.demo.common.config.kafka.event.PaymentCompletedEvent;

@EnableKafka
@Configuration
public class KafkaProduceConfig {

	@Value("${spring.kafka.bootstrap-servers}")
	private String bootStrapServers;

	@Bean
	public ProducerFactory<String, PaymentCompletedEvent> eventProducerFactory(){
		Map<String, Object> props = new HashMap<>();

		props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootStrapServers);

		props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
		props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JacksonJsonSerializer.class);

		return new DefaultKafkaProducerFactory<>(props);
	}

	// kafka템플릿 - Spring Boot <-> Kakfa 통신 가능하게함
	@Bean
	public KafkaTemplate<String, PaymentCompletedEvent> paymentCompletedEventKafkaTemplate(){

		// kakfa 템플릿을 사용하는 팩토리를 넣음
		return new KafkaTemplate<>(eventProducerFactory());
	}
}
