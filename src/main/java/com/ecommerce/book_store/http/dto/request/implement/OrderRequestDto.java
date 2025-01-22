package com.ecommerce.book_store.http.dto.request.implement;

import com.ecommerce.book_store.core.constant.OrderStatus;
import com.ecommerce.book_store.http.dto.request.AbstractRequestDto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
public class OrderRequestDto extends AbstractRequestDto {
    @Positive
    private Long userId;
    @Positive
    private Long voucherId;
    @NotBlank
    private String address;
    @NotBlank
    private String phone;
    @NotBlank
    private String note;
    @NotBlank
    private String status;

    public OrderRequestDto () {

    }

    public OrderRequestDto(Long userId, Long voucherId, String address, String phone, String note, String status) {
        this.userId = userId;
        this.voucherId = voucherId;
        this.address = address;
        this.phone = phone;
        this.note = note;
        this.status = status;
    }
}
