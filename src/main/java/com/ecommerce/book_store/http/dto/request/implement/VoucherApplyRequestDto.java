package com.ecommerce.book_store.http.dto.request.implement;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VoucherApplyRequestDto {
    private String code;
    private Double totalPrice;
}
