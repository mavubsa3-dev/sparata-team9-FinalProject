package com.example.demo.domain.payment.service;

import com.example.demo.common.entity.kafka.event.PaymentCompletedEvent;
import com.example.demo.common.exception.CustomException;
import com.example.demo.common.exception.ErrorCode;
import com.example.demo.domain.address.entity.Address;
import com.example.demo.domain.address.repository.AddressRepository;
import com.example.demo.domain.order.entity.Order;
import com.example.demo.domain.order.entity.OrderItem;
import com.example.demo.domain.order.repository.OrderRepository;
import com.example.demo.domain.payment.dto.request.CreatePaymentRequest;
import com.example.demo.domain.payment.dto.response.CreatePaymentResponse;
import com.example.demo.domain.payment.dto.response.GetPaymentResponse;
import com.example.demo.domain.payment.entity.Payment;
import com.example.demo.domain.payment.entity.PaymentStatus;
import com.example.demo.domain.payment.repository.PaymentRepository;
import com.example.demo.domain.ranking.service.RankingService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;


@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;
    private final AddressRepository addressRepository;
    private final RankingService rankingService;

    @Transactional
    public CreatePaymentResponse createPayment(Long userId, CreatePaymentRequest request) {
        Order order = orderRepository.findById(request.orderId())
                .orElseThrow(() -> new CustomException(ErrorCode.ORDER_NOT_FOUND));

        if (!order.getUser().getId().equals(userId)) {
            throw new CustomException(ErrorCode.ORDER_ACCESS_DENIED);
        }

        if (!order.isPaymentPending()) {
            throw new CustomException(ErrorCode.ORDER_NOT_PAYABLE);
        }

        Address address = addressRepository.findById(request.addressId())
                .orElseThrow(() -> new CustomException(ErrorCode.ADDRESS_NOT_FOUND));
        if (!address.getUser().getId().equals(userId)) {
            throw new CustomException(ErrorCode.ADDRESS_ACCESS_DENIED);
        }
        order.assignAddress(
                address.getName(),
                address.getPhoneNumber(),
                address.getZipCode(),
                address.getBasicAddress(),
                address.getDetailAddress()
        );

        if (paymentRepository.existsByOrderId(order.getId())) {
            throw new CustomException(ErrorCode.PAYMENT_ALREADY_EXISTS);
        }

        Payment payment = new Payment(order, order.getTotalProductAmount(), order.getTotalProductAmount());

        try {
            paymentRepository.save(payment);
        } catch (DataIntegrityViolationException e) {
            throw new CustomException(ErrorCode.PAYMENT_ALREADY_EXISTS);
        }

        order.getOrderItems()
            .forEach(item ->
                rankingService.increaseScore(item.getProduct().getId() + ":" + item.getProduct().getName(),
                    item.getQuantity()));

        return CreatePaymentResponse.from(payment);
    }

    public GetPaymentResponse getPayment(Long userId, Long paymentId) {
        Payment payment = paymentRepository.findDetailById(paymentId)
                .orElseThrow(() -> new CustomException(ErrorCode.PAYMENT_NOT_FOUND));

        if (!payment.getOrder().getUser().getId().equals(userId)) {
            throw new CustomException(ErrorCode.PAYMENT_ACCESS_DENIED);
        }

        return GetPaymentResponse.from(payment);
    }

    public List<GetPaymentResponse> getPayments(Long userId) {
        return paymentRepository.findAllByUserIdOrderByCreatedAtDesc(userId).stream()
                .map(GetPaymentResponse::from)
                .toList();
    }

    public List<GetPaymentResponse> getPaymentsForAdmin() {
        return paymentRepository.findAllOrderByCreatedAtDesc().stream()
                .map(GetPaymentResponse::from)
                .toList();
    }

    public GetPaymentResponse getPaymentForAdmin(Long paymentId) {
        Payment payment = paymentRepository.findDetailById(paymentId)
                .orElseThrow(() -> new CustomException(ErrorCode.PAYMENT_NOT_FOUND));

        return GetPaymentResponse.from(payment);
    }

    @Transactional
    public void cancelPaymentIfExists(Long orderId) {
        paymentRepository.findByOrderId(orderId)
                .ifPresent(Payment::cancel);
    }

    @Transactional
    public void cancelPayment(Long userId, Long paymentId) {
        Payment payment = paymentRepository.findDetailById(paymentId)
                .orElseThrow(() -> new CustomException(ErrorCode.PAYMENT_NOT_FOUND));

        if (!payment.getOrder().getUser().getId().equals(userId)) {
            throw new CustomException(ErrorCode.PAYMENT_ACCESS_DENIED);
        }

        if (payment.getStatus() != PaymentStatus.PENDING) {
            throw new CustomException(ErrorCode.PAYMENT_CANNOT_CANCEL);
        }

        Order order = payment.getOrder();
        for (OrderItem orderItem : order.getOrderItems()) {
            orderItem.getProduct().increaseStock(orderItem.getQuantity());
        }
        order.cancel();

        payment.cancel();
    }
}
