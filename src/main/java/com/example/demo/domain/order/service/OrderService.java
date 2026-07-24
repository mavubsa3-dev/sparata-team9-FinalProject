package com.example.demo.domain.order.service;

import com.example.demo.common.exception.CustomException;
import com.example.demo.common.exception.ErrorCode;
import com.example.demo.domain.address.entity.Address;
import com.example.demo.domain.address.repository.AddressRepository;
import com.example.demo.domain.cart.entity.CartItem;
import com.example.demo.domain.cart.repository.CartItemRepository;
import com.example.demo.domain.order.dto.CreateOrderRequest;
import com.example.demo.domain.order.dto.CreateOrderResponse;
import com.example.demo.domain.order.dto.GetOrderResponse;
import com.example.demo.domain.order.dto.GetOrderDetailResponse;
import com.example.demo.domain.order.entity.Order;
import com.example.demo.domain.order.entity.OrderItem;
import com.example.demo.domain.order.repository.OrderRepository;
import com.example.demo.domain.product.entity.Product;
import com.example.demo.domain.product.entity.ProductStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final CartItemRepository cartItemRepository;
    private final AddressRepository addressRepository;

    @Transactional
    public CreateOrderResponse createOrder(Long userId, CreateOrderRequest request) {
        List<CartItem> cartItems = cartItemRepository.findAllById(request.cartItemIds());
        validateCartItemsExist(cartItems, request.cartItemIds());
        validateCartItemsOwner(cartItems, userId);

        Address address = addressRepository.findById(request.addressId())
                .orElseThrow(() -> new CustomException(ErrorCode.ADDRESS_NOT_FOUND));
        validateAddressOwner(address, userId);

        Order order = new Order(
                cartItems.get(0).getUser(),
                generateOrderNumber(),
                address.getName(),
                address.getPhoneNumber(),
                address.getZipCode(),
                address.getBasicAddress(),
                address.getDetailAddress()
        );

        for (CartItem cartItem : cartItems) {
            Product product = cartItem.getProduct();
            validateProductOnSale(product);
            validateStock(product, cartItem.getQuantity());

            product.decreaseStock(cartItem.getQuantity());

            OrderItem orderItem = new OrderItem(
                    order,
                    product,
                    product.getName(),
                    product.getPrice(),
                    cartItem.getQuantity()
            );
            order.addOrderItem(orderItem);
        }

        Order savedOrder = orderRepository.save(order);
        cartItemRepository.deleteAll(cartItems);

        return CreateOrderResponse.from(savedOrder);
    }

    @Transactional(readOnly = true)
    public List<GetOrderResponse> getOrders(Long userId) {
        return orderRepository.findAllByUserIdOrderByCreatedAtDesc(userId).stream()
                .map(GetOrderResponse::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public GetOrderDetailResponse getOrder(Long userId, Long orderId) {
        Order order = orderRepository.findDetailByIdAndUserId(orderId, userId)
                .orElseThrow(() -> new CustomException(ErrorCode.ORDER_NOT_FOUND));
        return GetOrderDetailResponse.from(order);
    }

    private void validateCartItemsExist(List<CartItem> cartItems, List<Long> requestedIds) {
        if (cartItems.size() != requestedIds.size()) {
            throw new CustomException(ErrorCode.CART_ITEM_NOT_FOUND);
        }
    }

    private void validateCartItemsOwner(List<CartItem> cartItems, Long userId) {
        boolean hasAccessDenied = cartItems.stream()
                .anyMatch(cartItem -> !cartItem.getUser().getId().equals(userId));
        if (hasAccessDenied) {
            throw new CustomException(ErrorCode.CART_ITEM_ACCESS_DENIED);
        }
    }

    private void validateAddressOwner(Address address, Long userId) {
        if (!address.getUser().getId().equals(userId)) {
            throw new CustomException(ErrorCode.ADDRESS_ACCESS_DENIED);
        }
    }

    private void validateProductOnSale(Product product) {
        if (product.getStatus() != ProductStatus.ON_SALE) {
            throw new CustomException(ErrorCode.PRODUCT_NOT_ON_SALE);
        }
    }

    private void validateStock(Product product, Integer quantity) {
        if (product.getStock() == null || product.getStock() < quantity) {
            throw new CustomException(ErrorCode.ORDER_STOCK_SHORTAGE);
        }
    }

    private String generateOrderNumber() {
        return "ORD-" + LocalDateTime.now().toLocalDate() + "-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
}