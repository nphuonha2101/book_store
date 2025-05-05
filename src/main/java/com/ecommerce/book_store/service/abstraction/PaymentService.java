package com.ecommerce.book_store.service.abstraction;

public interface PaymentService {
    /**
     * Process a payment for a given order.
     *
     * @param orderId the ID of the order to be paid for
     * @param amount  the amount to be paid
     * @return a redirect URL for the payment gateway
     */
    String payment(Long orderId, Integer amount);
}
