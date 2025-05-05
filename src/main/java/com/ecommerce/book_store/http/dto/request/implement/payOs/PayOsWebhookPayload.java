package com.ecommerce.book_store.http.dto.request.implement.payOs;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PayOsWebhookPayload {
    private String code;
    private String desc;
    private boolean success;
    private String signature;
    private PayOSData data;

}
