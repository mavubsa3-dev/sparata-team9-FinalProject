package com.example.demo.domain.portone.controller;

import com.example.demo.common.config.PortOneProperties;
import com.example.demo.common.exception.CustomException;
import com.example.demo.common.exception.ErrorCode;
import com.example.demo.domain.payment.service.PaymentService;
import com.example.demo.domain.portone.dto.request.PortOneWebhookRequest;
import com.example.demo.domain.portone.webhook.PortOneWebhookVerifier;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class PortOneWebhookController {

    private final PortOneProperties portOneProperties;
    private final PortOneWebhookVerifier webhookVerifier;
    private final PaymentService paymentService;
    private final ObjectMapper objectMapper = new ObjectMapper()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    @PostMapping("/api/webhooks/portone")
    public ResponseEntity<Void> handleWebhook(
            @RequestHeader("webhook-id") String webhookId,
            @RequestHeader("webhook-timestamp") String webhookTimestamp,
            @RequestHeader("webhook-signature") String webhookSignature,
            @RequestBody String rawBody
    ) {
        String secret = portOneProperties.getWebhookSecret();
        log.info("[WEBHOOK DEBUG] secret length={}, secret prefix={}",
                secret == null ? -1 : secret.length(),
                secret == null ? "null" : secret.substring(0, Math.min(10, secret.length())));
        log.info("[WEBHOOK DEBUG] webhookId={}", webhookId);
        log.info("[WEBHOOK DEBUG] webhookTimestamp={}", webhookTimestamp);
        log.info("[WEBHOOK DEBUG] webhookSignature={}", webhookSignature);
        log.info("[WEBHOOK DEBUG] rawBody=[{}]", rawBody);

        boolean valid = webhookVerifier.verify(
                webhookId, webhookTimestamp, rawBody,
                webhookSignature, secret
        );

        log.info("[WEBHOOK DEBUG] valid={}", valid);

        if (!valid) {
            throw new CustomException(ErrorCode.WEBHOOK_SIGNATURE_INVALID);
        }

        PortOneWebhookRequest event;
        try {
            event = objectMapper.readValue(rawBody, PortOneWebhookRequest.class);
        } catch (Exception e) {
            throw new CustomException(ErrorCode.WEBHOOK_PAYLOAD_PARSE_FAILED);
        }

        if ("Transaction.Paid".equals(event.type())) {
            paymentService.handleWebhookPaid(event.data().paymentId());
        }

        return ResponseEntity.ok().build();
    }
}