package com.ecommerce.book_store.http.dto.request.implement;

import com.ecommerce.book_store.http.dto.request.AbstractRequestDto;
import jakarta.persistence.Entity;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderItemRequestDto extends AbstractRequestDto {
    @Positive
    private Long orderId;
    @Positive
    private Long bookId;
    @Positive
    private int quantity;
    @Positive
    private double price;
    public OrderItemRequestDto() {

    }

    public OrderItemRequestDto(Long orderId, Long bookId, int quantity, double price) {
        this.orderId = orderId;
        this.bookId = bookId;
        this.quantity = quantity;
        this.price = price;
    }
}
