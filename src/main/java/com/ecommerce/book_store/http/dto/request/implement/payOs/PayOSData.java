package com.ecommerce.book_store.http.dto.request.implement.payOs;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PayOSData {
    private Long orderCode;
    private Integer amount;
    private String description;
    private String accountNumber;
    private String reference;
    private String transactionDateTime;
    private String currency;
    private String paymentLinkId;
    private String code;
    private String desc;
}
