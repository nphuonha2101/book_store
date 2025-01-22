package com.ecommerce.book_store.http.dto.request.implement;

import com.ecommerce.book_store.http.dto.request.AbstractRequestDto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class VoucherRequestDto extends AbstractRequestDto {
    @NotBlank
    private String code;
    @Positive
    private double discount;
    @Positive
    private double minSpend;
    @NotNull
    private LocalDateTime expiredDate;
    public VoucherRequestDto() {

    }

    public VoucherRequestDto(String code, double discount, double minSpend, LocalDateTime expiredDate) {
        this.code = code;
        this.discount = discount;
        this.minSpend = minSpend;
        this.expiredDate = expiredDate;
    }
}
