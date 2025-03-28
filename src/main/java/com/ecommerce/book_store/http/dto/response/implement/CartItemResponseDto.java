package com.ecommerce.book_store.http.dto.response.implement;

import com.ecommerce.book_store.http.dto.response.AbstractResponseDto;
import lombok.Getter;

@Getter
public class CartItemResponseDto extends AbstractResponseDto {
    private final Long id;
    private final Long userId;
    private final BookResponseDto book;
    private final int quantity;
    private final double price;

    public CartItemResponseDto(Long id, Long userId, BookResponseDto book, int quantity, double price) {
        this.id = id;
        this.userId = userId;
        this.book = book;
        this.quantity = quantity;
        this.price = price;
    }
}
