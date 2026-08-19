package com.example.demo.domain.payment.service;

import com.example.demo.common.config.kafka.event.OrderItemInfo;
import com.example.demo.common.config.kafka.event.PaymentCompletedEvent;
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
import com.example.demo.domain.payment.dto.response.GetSettlementResponse;
import com.example.demo.domain.payment.entity.Payment;
import com.example.demo.domain.payment.entity.PaymentStatus;
import com.example.demo.domain.payment.entity.Settlement;
import com.example.demo.domain.payment.repository.PaymentRepository;
import com.example.demo.domain.payment.repository.PaymentSettlementSummary;
import com.example.demo.domain.payment.repository.SettlementRepository;
import com.example.demo.domain.portone.client.PortOneClient;
import com.example.demo.domain.portone.dto.PortOnePaymentResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class PaymentService {

    private static final ZoneId KST = ZoneId.of("Asia/Seoul");

    private final PaymentRepository paymentRepository;
    private final SettlementRepository settlementRepository;
    private final OrderRepository orderRepository;
    private final AddressRepository addressRepository;
    private final ApplicationEventPublisher eventPublisher;
    private final PortOneClient portOneClient;

    @Transactional
    public CreatePaymentResponse createPayment(Long userId, CreatePaymentRequest request) {
        Order order = orderRepository.findById(request.orderId())
            .orElseThrow(() -> new CustomException(ErrorCode.ORDER_NOT_FOUND));

        if (!order.getUser().getId().equals(userId)) {
            throw new CustomException(ErrorCode.ORDER_ACCESS_DENIED);
        }

        if (!order.isOrdered()) {
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

        publishPaymentCompletedEvent(payment);

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

    /**
     * 매일 00:00(KST)에 실행되어 "어제" 하루치 매출을 집계해서 저장한다.
     * settlement_date 유니크 제약 덕분에, 이미 그 날짜 정산이 있으면 갱신(update)하고
     * 없으면 새로 생성(insert)한다 — 재실행/중복 실행에도 안전하다(멱등).
     */
    @Scheduled(cron = "*/5 * * * * *", zone = "Asia/Seoul")
    @Transactional
    public void aggregateYesterdaySettlement() {
        LocalDate targetDate = LocalDate.now(KST).minusDays(1);
        Settlement settlement = aggregateSettlement(targetDate);

        log.info("[Settlement] date={}, totalAmount={}, orderCount={}",
                settlement.getSettlementDate(), settlement.getTotalAmount(), settlement.getOrderCount());
    }

    /**
     * 특정 날짜의 매출을 집계해서 저장한다(upsert).
     * 스케줄러뿐 아니라 관리자가 특정 날짜를 수동으로 재집계할 때도 재사용한다.
     */
    @Transactional
    public Settlement aggregateSettlement(LocalDate targetDate) {
        LocalDateTime start = targetDate.atStartOfDay();
        LocalDateTime end = start.plusDays(1);

        PaymentSettlementSummary summary = paymentRepository.getDailySettlementSummary(
                PaymentStatus.PAID, start, end
        );
        Long totalAmount = summary.getTotalAmount() != null ? summary.getTotalAmount() : 0L;
        Long orderCount = summary.getOrderCount() != null ? summary.getOrderCount() : 0L;

        return settlementRepository.findBySettlementDate(targetDate)
                .map(existing -> {
                    existing.update(totalAmount, orderCount);
                    return existing;
                })
                .orElseGet(() -> settlementRepository.save(
                        new Settlement(targetDate, totalAmount, orderCount)
                ));
    }

    public List<GetSettlementResponse> getSettlements() {
        return settlementRepository.findAllByOrderBySettlementDateDesc().stream()
                .map(GetSettlementResponse::from)
                .toList();
    }

    public GetSettlementResponse getSettlement(LocalDate settlementDate) {
        Settlement settlement = settlementRepository.findBySettlementDate(settlementDate)
                .orElseThrow(() -> new CustomException(ErrorCode.SETTLEMENT_NOT_FOUND));
        return GetSettlementResponse.from(settlement);
    }

    @Transactional
    public GetSettlementResponse aggregateSettlementForAdmin(LocalDate settlementDate) {
        return GetSettlementResponse.from(aggregateSettlement(settlementDate));
    }

    @Transactional
    public void cancelPayment(Long userId, Long paymentId) {
        Payment payment = paymentRepository.findDetailById(paymentId)
            .orElseThrow(() -> new CustomException(ErrorCode.PAYMENT_NOT_FOUND));

        if (!payment.getOrder().getUser().getId().equals(userId)) {
            throw new CustomException(ErrorCode.PAYMENT_ACCESS_DENIED);
        }

        if (payment.getStatus() != PaymentStatus.PENDING && payment.getStatus() != PaymentStatus.PAID) {
            throw new CustomException(ErrorCode.PAYMENT_CANNOT_CANCEL);
        }

        if (payment.getStatus() == PaymentStatus.PAID) {
            portOneClient.cancelPayment(payment.getPortonePaymentId(), "사용자 요청에 의한 결제 취소");
        }

        Order order = payment.getOrder();
        for (OrderItem orderItem : order.getOrderItems()) {
            orderItem.getProduct().increaseStock(orderItem.getQuantity());
        }
        order.cancel();

        payment.cancel();
    }

    @Transactional
    public void confirmPayment(Long userId, Long paymentId, String portonePaymentId) {
        Payment payment = paymentRepository.findDetailById(paymentId)
            .orElseThrow(() -> new CustomException(ErrorCode.PAYMENT_NOT_FOUND));

        if (!payment.getOrder().getUser().getId().equals(userId)) {
            throw new CustomException(ErrorCode.PAYMENT_ACCESS_DENIED);
        }

        if (payment.getStatus() != PaymentStatus.PENDING) {
            throw new CustomException(ErrorCode.PAYMENT_NOT_APPROVABLE);
        }

        PortOnePaymentResponse portOneResponse = portOneClient.getPayment(portonePaymentId);

        if (!payment.getPaymentAmount().equals(portOneResponse.amount().total())) {
            throw new CustomException(ErrorCode.PAYMENT_AMOUNT_MISMATCH);
        }

        payment.approve(portOneResponse.id(), LocalDateTime.now());
        payment.getOrder().complete();

        publishPaymentCompletedEvent(payment);
    }

    private static final Pattern PORTONE_PAYMENT_ID_PATTERN = Pattern.compile("^pay(\\d+)-");

    /**
     * 웹훅으로 결제 승인 이벤트가 들어왔을 때 처리.
     * 프론트에서 결제창 호출 시 만든 커스텀 paymentId(예: "pay9-a1b2c3d4")에서
     * 우리 DB의 payment.id를 추출해 매칭한다.
     * userId 검증은 하지 않는다 (서버-서버 통신이므로 로그인 사용자 컨텍스트가 없음).
     * 이미 PAID/CANCELED 상태면 중복 처리 방지를 위해 그대로 종료한다(멱등 처리).
     */
    @Transactional
    public void handleWebhookPaid(String portonePaymentId) {
        Matcher matcher = PORTONE_PAYMENT_ID_PATTERN.matcher(portonePaymentId);
        if (!matcher.find()) {
            throw new CustomException(ErrorCode.WEBHOOK_PAYMENT_ID_FORMAT_INVALID);
        }
        Long paymentId = Long.parseLong(matcher.group(1));

        Payment payment = paymentRepository.findDetailById(paymentId)
            .orElseThrow(() -> new CustomException(ErrorCode.PAYMENT_NOT_FOUND));

        if (payment.getStatus() == PaymentStatus.PAID || payment.getStatus() == PaymentStatus.CANCELED) {
            return;
        }

        PortOnePaymentResponse portOneResponse = portOneClient.getPayment(portonePaymentId);

        if (!payment.getPaymentAmount().equals(portOneResponse.amount().total())) {
            throw new CustomException(ErrorCode.PAYMENT_AMOUNT_MISMATCH);
        }

        payment.approve(portOneResponse.id(), LocalDateTime.now());
        payment.getOrder().complete();

        publishPaymentCompletedEvent(payment);
    }

    private void publishPaymentCompletedEvent(Payment payment) {
        Order order = payment.getOrder();

        String address1 = order.getAddress1();
        String address2 = order.getAddress2();

        List<OrderItemInfo> orderItems = order.getOrderItems().stream()
            .map(item -> new OrderItemInfo(
                item.getProduct().getId(),
                item.getProductName(),
                item.getQuantity()
            ))
            .toList();

        PaymentCompletedEvent event = PaymentCompletedEvent.builder()
            .paymentId(payment.getId())
            .userId(order.getUser().getId())
            .orderId(order.getId())
            .orderNumber(order.getOrderNumber())
            .totalAmount(payment.getPaymentAmount())
            .address(address1 + " " + address2)
            .orderItems(orderItems)
            .completedAt(LocalDateTime.now())
            .build();

        eventPublisher.publishEvent(event);
    }
}
