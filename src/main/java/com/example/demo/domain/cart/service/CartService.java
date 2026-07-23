package com.example.demo.domain.cart.service;

import com.example.demo.common.exception.CustomException;
import com.example.demo.common.exception.ErrorCode;
import com.example.demo.domain.cart.dto.response.GetCartResponse;
import com.example.demo.domain.cart.entity.Cart;
import com.example.demo.domain.cart.entity.CartItem;
import com.example.demo.domain.cart.repository.CartItemRepository;
import com.example.demo.domain.cart.repository.CartRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;

    public GetCartResponse getCart(Long userId) {
        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.CART_NOT_FOUND));

        List<CartItem> cartItems = cartItemRepository.findByCartIdWithProduct(cart.getId());

        return GetCartResponse.of(cart, cartItems);
    }
}