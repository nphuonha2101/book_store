package com.ecommerce.book_store.http.dto.request.implement;

import com.ecommerce.book_store.core.constant.OrderStatus;
import com.ecommerce.book_store.http.dto.request.AbstractRequestDto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class OrderRequestDto extends AbstractRequestDto {
    private Long userId;
    private Long voucherId;
    private Long addressId;
    private String phone;
    private String status;
    private String note;
    private Double totalAmount;
    private Double totalDiscount;
    List<OrderItemRequestDto> orderItems;
    private Integer paymentMethod;
    private String cancellationReason;
}
