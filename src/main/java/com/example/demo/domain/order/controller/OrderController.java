package com.example.demo.domain.order.controller;

import com.example.demo.domain.order.dto.CreateOrderRequest;
import com.example.demo.domain.order.dto.CreateOrderResponse;
import com.example.demo.domain.order.dto.GetOrderDetailResponse;
import com.example.demo.domain.order.dto.GetOrderResponse;
import com.example.demo.domain.order.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;


    @PostMapping
    public ResponseEntity<CreateOrderResponse> createOrder(
            @Valid @RequestBody CreateOrderRequest request
    ) {
        CreateOrderResponse response = orderService.createOrder(getCurrentUserId(), request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    @GetMapping
    public ResponseEntity<List<GetOrderResponse>> getOrders() {
        List<GetOrderResponse> response = orderService.getOrders(getCurrentUserId());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<GetOrderDetailResponse> getOrder(@PathVariable Long orderId) {
        GetOrderDetailResponse response = orderService.getOrder(getCurrentUserId(), orderId);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{orderId}/cancel")
    public ResponseEntity<Void> cancelOrder(@PathVariable Long orderId) {
        orderService.cancelOrder(getCurrentUserId(), orderId);
        return ResponseEntity.noContent().build();
    }

    private Long getCurrentUserId() {
        return (Long) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}