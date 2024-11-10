package com.ecommerce.book_store.http.dto.response;

import com.ecommerce.book_store.persistent.entity.Book;
import com.ecommerce.book_store.persistent.entity.BookImage;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class BookResponseDto {
    private final Long id;
    private final String title;
    private final String author;
    private final String description;
    private final String isbn;
    private final List<String> imageUrls;
    private final double price;
    private final int quantity;
    private final boolean isAvailable;

    public BookResponseDto(Long id, String title, String author, String description, String isbn, List<BookImage> bookImages, double price, int quantity, boolean isAvailable) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.description = description;
        this.isbn = isbn;
        this.imageUrls = bookImages.stream().map(BookImage::getUrl).toList();
        this.price = price;
        this.quantity = quantity;
        this.isAvailable = isAvailable;
    }

    public static List<BookResponseDto> toResponseDto(List<Book> entities) {
        return entities.stream().map(book -> new BookResponseDto(
                book.getId(),
                book.getTitle(),
                book.getAuthor(),
                book.getDescription(),
                book.getIsbn(),
                book.getImages(),
                book.getPrice(),
                book.getQuantity(),
                book.isAvailable()
                )).toList();
    }
}
