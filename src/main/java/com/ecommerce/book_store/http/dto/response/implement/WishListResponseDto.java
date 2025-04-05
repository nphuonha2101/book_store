package com.ecommerce.book_store.http.dto.response.implement;

import com.ecommerce.book_store.http.dto.response.AbstractResponseDto;
import lombok.Getter;

@Getter
public class WishListResponseDto extends AbstractResponseDto {
    private final Long id;
    private final Long userId;
    private final BookResponseDto book;

    public WishListResponseDto(Long id, Long userId, BookResponseDto book) {
        this.id = id;
        this.userId = userId;
        this.book = book;
    }
}

