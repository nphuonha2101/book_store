package com.ecommerce.book_store.http.dto.response.implement;

import com.ecommerce.book_store.http.dto.response.AbstractResponseDto;
import lombok.Getter;

@Getter
public class ReviewResponseDto extends AbstractResponseDto {
    private final Long id;
    private final UserResponseDto user;
    private final BookResponseDto book;
    private final int rating;
    private final String comment;

    public ReviewResponseDto(Long id, UserResponseDto user, BookResponseDto book, int rating, String comment) {
        this.id = id;
        this.user = user;
        this.book = book;
        this.rating = rating;
        this.comment = comment;
    }


}
