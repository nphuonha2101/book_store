package com.ecommerce.book_store.http.dto.response.implement;

import com.ecommerce.book_store.http.dto.response.AbstractResponseDto;
import com.ecommerce.book_store.persistent.entity.BookImage;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BookResponseDto extends AbstractResponseDto {
    private Long id;
    private String title;
    private String authorName;
    private String description;
    private String isbn;
    private List<BookImageResponseDto> images;
    private String coverImage;
    private int  price;
    private int quantity;
    private boolean isAvailable;
    private LocalDateTime publishedAt;
    private CategoryResponseDto category;
}
