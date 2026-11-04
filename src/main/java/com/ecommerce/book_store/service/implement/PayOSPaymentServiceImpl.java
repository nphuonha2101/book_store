package com.ecommerce.book_store.service.implement;

import com.ecommerce.book_store.service.abstraction.PaymentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import vn.payos.PayOS;
import vn.payos.type.CheckoutResponseData;
import vn.payos.type.PaymentData;

@Slf4j
@Service
@Qualifier("payOSPaymentService")
public class PayOSPaymentServiceImpl implements PaymentService {
    private final PayOS payOS;
    private final String returnUrl;
    private final String cancelUrl;

    public PayOSPaymentServiceImpl(
            PayOS payOS,
            @Value("${react.client.url}") String clientUrl) {
        this.payOS = payOS;
        this.returnUrl = clientUrl + "/order-success/";
        this.cancelUrl = clientUrl + "/order-failed/";
    }

    @Override
    public String payment(Long orderId, Integer amount) {
        try {
            PaymentData paymentData = PaymentData.builder()
                    .orderCode(orderId)
                    .amount(amount)
                    .description("NPBookStore #" + orderId)
                    .returnUrl(returnUrl + orderId)
                    .cancelUrl(cancelUrl + orderId)
                    .build();

            CheckoutResponseData checkoutResponseData = payOS.createPaymentLink(paymentData);
            return checkoutResponseData.getCheckoutUrl();
        } catch (Exception e) {
            log.error("Error while creating payment link for order {}", orderId, e);
            return null;
        }
    }
}
