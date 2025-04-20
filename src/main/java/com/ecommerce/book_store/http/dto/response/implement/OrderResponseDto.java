package com.ecommerce.book_store.http.dto.response.implement;

import com.ecommerce.book_store.http.dto.response.AbstractResponseDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderResponseDto extends AbstractResponseDto {
    private Long id;
    private UserResponseDto user;
    private VoucherResponseDto voucher;
    private AddressResponseDto address;
    private String phone;
    private String note;
    private String status;
    private double totalAmount;
    private double totalDiscount;
    private List<OrderItemResponseDto> orderItems;
    private Integer paymentMethod;
}
