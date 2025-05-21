package com.ecommerce.book_store.http.dto.response.implement;

import com.ecommerce.book_store.http.dto.response.AbstractResponseDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CartItemResponseDto extends AbstractResponseDto {
    private Long id;
    private Long userId;
    private BookResponseDto book;
    private int quantity;
    private double price;
    private Long bookId;
}
