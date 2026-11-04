package com.ecommerce.book_store.http.controller.api.webhook.payment;

import com.ecommerce.book_store.core.constant.OrderStatus;
import com.ecommerce.book_store.http.dto.request.implement.payOs.PayOsWebhookPayload;
import com.ecommerce.book_store.service.abstraction.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.payos.PayOS;
import vn.payos.type.Webhook;
import vn.payos.type.WebhookData;

@Slf4j
@RestController
@RequestMapping("/api/v1/webhook/payos")
public class PayOSWebHookController {
    private final OrderService orderService;
    private final PayOS payOS;

    public PayOSWebHookController(OrderService orderService, PayOS payOS) {
        this.orderService = orderService;
        this.payOS = payOS;
    }

    @PostMapping
    public ResponseEntity<Void> handlePayOSWebHook(@RequestBody PayOsWebhookPayload payload) {
        if (payload == null || payload.getData() == null || payload.getSignature() == null) {
            return ResponseEntity.badRequest().build();
        }

        try {
            // Verifies the HMAC signature using PAYOS_CHECKSUM_KEY. Without this
            // check anyone could mark orders as paid.
            Webhook webhook = Webhook.builder()
                    .code(payload.getCode())
                    .desc(payload.getDesc())
                    .success(payload.isSuccess())
                    .signature(payload.getSignature())
                    .data(WebhookData.builder()
                            .orderCode(payload.getData().getOrderCode())
                            .amount(payload.getData().getAmount())
                            .description(payload.getData().getDescription())
                            .accountNumber(payload.getData().getAccountNumber())
                            .reference(payload.getData().getReference())
                            .transactionDateTime(payload.getData().getTransactionDateTime())
                            .currency(payload.getData().getCurrency())
                            .paymentLinkId(payload.getData().getPaymentLinkId())
                            .code(payload.getData().getCode())
                            .desc(payload.getData().getDesc())
                            .build())
                    .build();

            WebhookData data = payOS.verifyPaymentWebhookData(webhook);
            Long orderId = data.getOrderCode();
            boolean success = Boolean.TRUE.equals(webhook.getSuccess())
                    && "00".equals(webhook.getCode());

            log.info("PayOS webhook verified: orderId={}, success={}", orderId, success);
            orderService.updateOrderStatus(orderId,
                    success ? OrderStatus.PROCESSING : OrderStatus.FAILED);

            return ResponseEntity.ok().build();
        } catch (Exception e) {
            log.warn("Rejected PayOS webhook: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }
}
