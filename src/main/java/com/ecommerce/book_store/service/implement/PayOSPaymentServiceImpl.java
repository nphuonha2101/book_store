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
    @Value("${payment.payos.client-id}")
    private String CLIENT_ID;
    @Value("${payment.payos.checksum-key}")
    private String CHECKSUM;
    @Value("${payment.payos.api-key}")
    private String API_KEY;
    @Value("${react.client.url}")
    private String REACT_CLIENT_URL;

    @Override
    public String payment(Long orderId, Integer amount) {
        try {
            final PayOS payOS = new PayOS(CLIENT_ID, API_KEY, CHECKSUM);

            PaymentData paymentData = PaymentData
                    .builder()
                    .orderCode(orderId)
                    .amount(amount)
                    .description("NPBookStore #" + orderId)
                    .returnUrl(REACT_CLIENT_URL + "/order-success/" + orderId)
                    .cancelUrl(REACT_CLIENT_URL + "/order-failed/" + orderId)
                    .build();

            CheckoutResponseData checkoutResponseData = payOS.createPaymentLink(paymentData);

            return checkoutResponseData.getCheckoutUrl();
        } catch (Exception e) {
            log.error("Error while creating payment link: ", e);
            return null;
        }
    }
}

