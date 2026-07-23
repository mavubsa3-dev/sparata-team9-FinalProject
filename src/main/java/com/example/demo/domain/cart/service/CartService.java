package com.example.demo.domain.cart.service;

import com.example.demo.common.exception.CustomException;
import com.example.demo.common.exception.ErrorCode;
import com.example.demo.domain.cart.dto.request.AddCartItemRequest;
import com.example.demo.domain.cart.dto.response.AddCartItemResponse;
import com.example.demo.domain.cart.dto.response.GetCartResponse;
import com.example.demo.domain.cart.entity.Cart;
import com.example.demo.domain.cart.entity.CartItem;
import com.example.demo.domain.cart.repository.CartItemRepository;
import com.example.demo.domain.cart.repository.CartRepository;
import com.example.demo.domain.product.entity.Product;
import com.example.demo.domain.product.repository.ProductRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;

    public GetCartResponse getCart(Long userId) {
        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.CART_NOT_FOUND));

        List<CartItem> cartItems = cartItemRepository.findByCartIdWithProduct(cart.getId());

        return GetCartResponse.of(cart, cartItems);
    }

    @Transactional
    public AddCartItemResponse addCartItem(Long userId, AddCartItemRequest request) {
        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.CART_NOT_FOUND));

        Product product = productRepository.findByIdWithCategory(request.productId())
                .orElseThrow(() -> new CustomException(ErrorCode.PRODUCT_NOT_FOUND));

        if (!product.isPurchasable()) {
            throw new CustomException(ErrorCode.PRODUCT_NOT_PURCHASABLE);
        }

        CartItem cartItem = cartItemRepository.findByCartAndProduct(cart, product)
                .orElse(null);

        int totalQuantity = (cartItem == null)
                ? request.quantity()
                : cartItem.getQuantity() + request.quantity();

        if (product.getStock() < totalQuantity) {
            throw new CustomException(ErrorCode.INSUFFICIENT_STOCK);
        }

        if (cartItem == null) {
            cartItem = createCartItem(cart, product, request.quantity());
        } else {
            cartItem.increaseQuantity(request.quantity());
        }

        return AddCartItemResponse.from(cartItem);
    }

    private CartItem createCartItem(Cart cart, Product product, int quantity) {
        try {
            CartItem newCartItem = new CartItem(cart, cart.getUser(), product, quantity);
            cartItemRepository.save(newCartItem);
            return newCartItem;
        } catch (DataIntegrityViolationException e) {
            throw new CustomException(ErrorCode.DUPLICATE_CART_ITEM);
        }
    }
}