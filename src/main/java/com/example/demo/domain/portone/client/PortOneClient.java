package com.example.demo.domain.portone.client;

import com.example.demo.domain.portone.dto.PortOnePaymentResponse;
import com.example.demo.domain.portone.dto.request.PortOneCancelRequest;
import com.example.demo.domain.portone.dto.response.PortOneCancelResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
@RequiredArgsConstructor
public class PortOneClient {

    private final RestClient portOneRestClient;

    public PortOnePaymentResponse getPayment(String paymentId) {
        return portOneRestClient.get()
                .uri("/payments/{paymentId}", paymentId)
                .retrieve()
                .body(PortOnePaymentResponse.class);
    }

    public PortOneCancelResponse cancelPayment(String paymentId, String reason) {
        return portOneRestClient.post()
                .uri("/payments/{paymentId}/cancel", paymentId)
                .body(new PortOneCancelRequest(reason))
                .retrieve()
                .body(PortOneCancelResponse.class);
    }
}