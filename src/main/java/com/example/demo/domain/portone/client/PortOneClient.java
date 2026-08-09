package com.example.demo.domain.portone.client;

import com.example.demo.common.config.PortOneProperties;
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
    private final PortOneProperties portOneProperties;

    public PortOnePaymentResponse getPayment(String paymentId) {
        return portOneRestClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/payments/{paymentId}")
                        .queryParam("storeId", portOneProperties.getStoreId())
                        .build(paymentId))
                .retrieve()
                .body(PortOnePaymentResponse.class);
    }

    public PortOneCancelResponse cancelPayment(String paymentId, String reason) {
        return portOneRestClient.post()
                .uri("/payments/{paymentId}/cancel", paymentId)
                .body(new PortOneCancelRequest(reason, portOneProperties.getStoreId()))
                .retrieve()
                .body(PortOneCancelResponse.class);
    }
}