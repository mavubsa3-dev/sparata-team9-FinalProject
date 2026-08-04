package com.example.demo.domain.notification.service;

import com.example.demo.domain.notification.dto.request.SendEmailMessageRequest;

public interface EmailService {

	void send(SendEmailMessageRequest sendEmailMessageRequest);
}
