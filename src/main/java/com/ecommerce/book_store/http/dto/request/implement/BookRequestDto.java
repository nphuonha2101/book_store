package com.ecommerce.book_store.http.dto.request.implement;

import com.ecommerce.book_store.http.dto.request.AbstractRequestDto;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import jakarta.validation.constraints.*;

@Getter
@Setter
public class BookRequestDto extends AbstractRequestDto {
    @NotBlank
    private String title;
    @NotBlank
    private String authorName;
    private String description;
    @Min(10)
    private String isbn;
    @Positive
    private double price;
    @Positive
    private int quantity;
    private boolean isAvailable;
    @Positive
    private Long categoryId;
    @NotNull
    private LocalDateTime publishedAt;
    private List<String> imageUrls;

    public BookRequestDto() {
    }

    public BookRequestDto(String title, String authorName, String description, String isbn, double price, int quantity, boolean isAvailable, Long categoryId, LocalDateTime publishedAt, List<String> imageUrls) {
        this.title = title;
        this.authorName = authorName;
        this.description = description;
        this.isbn = isbn;
        this.price = price;
        this.quantity = quantity;
        this.isAvailable = isAvailable;
        this.categoryId = categoryId;
        this.publishedAt = publishedAt;
        this.imageUrls = imageUrls;
    }
}
