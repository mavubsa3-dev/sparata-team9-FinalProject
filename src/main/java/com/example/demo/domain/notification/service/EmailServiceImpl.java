package com.example.demo.domain.notification.service;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.example.demo.domain.notification.dto.request.SendEmailMessageRequest;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService{

	private final JavaMailSender javaMailSender;

	@Override
	public void send(SendEmailMessageRequest sendEmailMessageRequest){

		MimeMessage message = javaMailSender.createMimeMessage();

		try{
			// 현재는 첨부파일 추가 x, false 설정
			MimeMessageHelper messageHelper = new MimeMessageHelper(message, false, "UTF-8");
			messageHelper.setTo(sendEmailMessageRequest.to());
			messageHelper.setSubject(sendEmailMessageRequest.subject());
			// 두 번째 파라미터 html 여부 확인
			messageHelper.setText(sendEmailMessageRequest.message(), true);
			javaMailSender.send(message);
			log.info("[이메일 전송] : {} ", sendEmailMessageRequest.to());

		} catch (MessagingException e) {
			throw new RuntimeException("이메일 전송 중 오류 발생", e);
		}
	}
}
