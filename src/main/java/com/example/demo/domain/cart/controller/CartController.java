package com.example.demo.domain.cart.controller;

import com.example.demo.domain.cart.dto.request.AddCartItemRequest;
import com.example.demo.domain.cart.dto.request.UpdateCartItemRequest;
import com.example.demo.domain.cart.dto.response.AddCartItemResponse;
import com.example.demo.domain.cart.dto.response.GetCartResponse;
import com.example.demo.domain.cart.dto.response.UpdateCartItemResponse;
import com.example.demo.domain.cart.service.CartService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/carts")
public class CartController {

    private final CartService cartService;

    // 로그인 기능 완성되면 @RequestParam Long userId를 인증 정보에서 추출하는 방식으로 교체
    @GetMapping
    public ResponseEntity<GetCartResponse> getCart(@RequestParam Long userId) {
        return ResponseEntity.ok(cartService.getCart(userId));
    }

    // 로그인 기능 완성되면 @RequestParam Long userId를 인증 정보에서 추출하는 방식으로 교체
    @PostMapping("/items")
    public ResponseEntity<AddCartItemResponse> addCartItem(
            @RequestParam Long userId,
            @Valid @RequestBody AddCartItemRequest request
    ) {
        AddCartItemResponse response = cartService.addCartItem(userId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // 로그인 기능 완성되면 @RequestParam Long userId를 인증 정보에서 추출하는 방식으로 교체
    @PatchMapping("/items/{cartItemId}")
    public ResponseEntity<UpdateCartItemResponse> updateCartItem(
            @RequestParam Long userId,
            @PathVariable Long cartItemId,
            @Valid @RequestBody UpdateCartItemRequest request
    ) {
        UpdateCartItemResponse response = cartService.updateCartItem(userId, cartItemId, request);
        return ResponseEntity.ok(response);
    }
}