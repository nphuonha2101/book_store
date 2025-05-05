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

@Slf4j
@RestController
@RequestMapping("/api/v1/webhook/payos")
public class PayOSWebHookController {
    private final OrderService orderService;

    public PayOSWebHookController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public ResponseEntity<Void> handlePayOSWebHook(
             @RequestBody PayOsWebhookPayload payload) {
        try {
            if (payload == null || payload.getCode() == null || !"00".equals(payload.getCode()))
                return ResponseEntity.badRequest().build();

            Long orderId = payload.getData().getOrderCode();
            boolean isSuccess = payload.isSuccess();

            log.info("PayOS Webhook received: orderId = {}, success = {}", orderId, isSuccess);

            if (isSuccess)
                orderService.updateOrderStatus(orderId, OrderStatus.PROCESSING);
            else
                orderService.updateOrderStatus(orderId, OrderStatus.FAILED);

            return ResponseEntity.ok().build();
        } catch (Exception e) {
            log.error("PayOS Webhook Error: {}", e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }
}
