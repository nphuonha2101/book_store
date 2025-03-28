package com.ecommerce.book_store.http.dto.request.implement;

import com.ecommerce.book_store.http.dto.request.AbstractRequestDto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CartItemRequestDto extends AbstractRequestDto {
    private Long userId;
    private Long bookId;
    private int quantity;
    private double price;


}
