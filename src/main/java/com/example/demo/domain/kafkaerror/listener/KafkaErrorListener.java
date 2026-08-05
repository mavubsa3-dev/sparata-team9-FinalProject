package com.example.demo.domain.kafkaerror.listener;

import static com.example.demo.common.config.kafka.topic.KafkaTopic.*;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class KafkaErrorListener {

	@KafkaListener(
		topics = TOPIC_KAFKA_ERROR,
		groupId = "kafka-error",
		containerFactory = "errorKafkaListenerContainerFactory"
	) public void consumer(String message){
		log.info("[kafka-error] 받은 메세지 : {} ", message);

		if (message.contains("error")){
			log.info("에러 발생 메세지 : {} ", message);
			throw new RuntimeException("테스트용 에러");
		}
	}
}
