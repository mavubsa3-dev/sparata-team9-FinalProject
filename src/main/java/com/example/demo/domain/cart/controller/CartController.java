package com.example.demo.domain.cart.controller;

import com.example.demo.domain.cart.dto.request.AddCartItemRequest;
import com.example.demo.domain.cart.dto.request.UpdateCartItemRequest;
import com.example.demo.domain.cart.dto.response.AddCartItemResponse;
import com.example.demo.domain.cart.dto.response.GetCartItemCountResponse;
import com.example.demo.domain.cart.dto.response.GetCartResponse;
import com.example.demo.domain.cart.dto.response.UpdateCartItemResponse;
import com.example.demo.domain.cart.service.CartService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/carts")
public class CartController {

    private final CartService cartService;

    @GetMapping
    public ResponseEntity<GetCartResponse> getCart(@AuthenticationPrincipal Long userId) {
        return ResponseEntity.ok(cartService.getCart(userId));
    }

    @GetMapping("/count")
    public ResponseEntity<GetCartItemCountResponse> getCartItemCount(@AuthenticationPrincipal Long userId) {
        return ResponseEntity.ok(cartService.getCartItemCount(userId));
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

    @DeleteMapping("/items/{cartItemId}")
    public ResponseEntity<Void> deleteCartItem(
            @AuthenticationPrincipal Long userId,
            @PathVariable Long cartItemId
    ) {
        cartService.deleteCartItem(userId, cartItemId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/clear")
    public ResponseEntity<Void> clearCart(@AuthenticationPrincipal Long userId) {
        cartService.clearCart(userId);
        return ResponseEntity.noContent().build();
    }
}