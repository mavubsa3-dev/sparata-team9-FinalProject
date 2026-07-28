package com.example.demo.domain.payment.controller;

import com.example.demo.domain.payment.dto.response.GetPaymentResponse;
import com.example.demo.domain.payment.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/payments")
public class AdminPaymentController {

    private final PaymentService paymentService;

    @GetMapping
    public ResponseEntity<List<GetPaymentResponse>> getPayments() {
        return ResponseEntity.ok(paymentService.getPaymentsForAdmin());
    }

    @GetMapping("/{paymentId}")
    public ResponseEntity<GetPaymentResponse> getPayment(@PathVariable Long paymentId) {
        return ResponseEntity.ok(paymentService.getPaymentForAdmin(paymentId));
    }
}