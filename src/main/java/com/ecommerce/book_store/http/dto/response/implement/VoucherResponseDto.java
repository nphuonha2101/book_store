package com.ecommerce.book_store.http.dto.response.implement;

import com.ecommerce.book_store.http.dto.response.AbstractResponseDto;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class VoucherResponseDto extends AbstractResponseDto {
    private final Long id;
    private final String code;
    private final double discount;
    private final double minSpend;
    private final LocalDateTime expiredDate;

    public VoucherResponseDto(Long id, String code, double discount, double minSpend, LocalDateTime expiredDate) {
        this.id = id;
        this.code = code;
        this.discount = discount;
        this.minSpend = minSpend;
        this.expiredDate = expiredDate;
    }
}
