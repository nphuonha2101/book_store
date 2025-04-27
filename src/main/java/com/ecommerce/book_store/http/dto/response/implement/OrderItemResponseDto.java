package com.ecommerce.book_store.http.dto.response.implement;

import com.ecommerce.book_store.http.dto.response.AbstractResponseDto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderItemResponseDto extends AbstractResponseDto {
    private final Long id;
    private final Long orderId;
    private final BookResponseDto book;
    private final int quantity;
    private final double price;

    public OrderItemResponseDto(Long id, Long orderId, BookResponseDto book, int quantity, double price) {
        this.id = id;
        this.orderId = orderId;
        this.book = book;
        this.quantity = quantity;
        this.price = price;
    }
}
