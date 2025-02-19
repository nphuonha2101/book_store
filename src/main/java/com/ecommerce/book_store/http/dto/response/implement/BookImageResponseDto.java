package com.ecommerce.book_store.http.dto.response.implement;

import com.ecommerce.book_store.http.dto.response.AbstractResponseDto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BookImageResponseDto extends AbstractResponseDto {
    private Long id;
    private Long bookId;
    private String url;

    public BookImageResponseDto(Long id, Long bookId, String url) {
        this.id = id;
        this.bookId = bookId;
        this.url = url;
    }

}
