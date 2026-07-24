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
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;


    @PostMapping
    public ResponseEntity<CreateOrderResponse> createOrder(
            @LoginUser Long userId,
            @Valid @RequestBody CreateOrderRequest request
    ) {
        CreateOrderResponse response = orderService.createOrder(userId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    @GetMapping
    public ResponseEntity<List<GetOrderResponse>> getOrders(@LoginUser Long userId) {
        List<GetOrderResponse> response = orderService.getOrders(userId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<GetOrderDetailResponse> getOrder(
            @LoginUser Long userId,
            @PathVariable Long orderId
    ) {
        GetOrderDetailResponse response = orderService.getOrder(userId, orderId);
        return ResponseEntity.ok(response);
    }
}