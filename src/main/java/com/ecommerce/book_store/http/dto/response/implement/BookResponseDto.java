package com.ecommerce.book_store.http.dto.response.implement;

import com.ecommerce.book_store.http.dto.response.AbstractResponseDto;
import com.ecommerce.book_store.persistent.entity.Book;
import com.ecommerce.book_store.persistent.entity.BookImage;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class BookResponseDto extends AbstractResponseDto {
    private final Long id;
    private final String title;
    private final String author;
    private final String description;
    private final String isbn;
    private final List<String> imageUrls;
    private final double price;
    private final int quantity;
    private final boolean isAvailable;
    private final LocalDateTime publishedAt;
    private final CategoryResponseDto category;

    public BookResponseDto(Long id, String title, String author, String description, String isbn, List<BookImage> bookImages, double price, int quantity, boolean isAvailable, CategoryResponseDto category, LocalDateTime publishedAt) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.description = description;
        this.isbn = isbn;
        this.imageUrls = bookImages.stream().map(BookImage::getUrl).toList();
        this.price = price;
        this.quantity = quantity;
        this.isAvailable = isAvailable;
        this.category = category;
        this.publishedAt = publishedAt;

    }


}
