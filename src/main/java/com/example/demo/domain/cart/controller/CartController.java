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
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/carts")
public class CartController {

    private final CartService cartService;

    @GetMapping
    public ResponseEntity<GetCartResponse> getCart(@AuthenticationPrincipal Long userId) {
        return ResponseEntity.ok(cartService.getCart(userId));
    }

    @PostMapping("/items")
    public ResponseEntity<AddCartItemResponse> addCartItem(
            @AuthenticationPrincipal Long userId,
            @Valid @RequestBody AddCartItemRequest request
    ) {
        AddCartItemResponse response = cartService.addCartItem(userId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PatchMapping("/items/{cartItemId}")
    public ResponseEntity<UpdateCartItemResponse> updateCartItem(
            @AuthenticationPrincipal Long userId,
            @PathVariable Long cartItemId,
            @Valid @RequestBody UpdateCartItemRequest request
    ) {
        UpdateCartItemResponse response = cartService.updateCartItem(userId, cartItemId, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/clear")
    public ResponseEntity<Void> clearCart(@AuthenticationPrincipal Long userId) {
        cartService.clearCart(userId);
        return ResponseEntity.noContent().build();
    }
}