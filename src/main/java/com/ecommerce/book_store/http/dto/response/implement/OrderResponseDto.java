package com.ecommerce.book_store.http.dto.response.implement;

import com.ecommerce.book_store.http.dto.response.AbstractResponseDto;
import lombok.Getter;

@Getter
public class OrderResponseDto extends AbstractResponseDto {
    private final Long id;
    private final UserResponseDto user;
    private final VoucherResponseDto voucher;
    private final String address;
    private final String phone;
    private final String note;
    private final String status;
    private final double totalAmount;


    public OrderResponseDto(Long id, UserResponseDto user, VoucherResponseDto voucher, String address, String phone, String note, String status, double totalAmount) {
        this.id = id;
        this.user = user;
        this.voucher = voucher;
        this.address = address;
        this.phone = phone;
        this.note = note;
        this.status = status;
        this.totalAmount = totalAmount;
    }
}
