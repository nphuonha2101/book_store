package com.ecommerce.book_store.http.dto.request.implement;

import com.ecommerce.book_store.http.dto.request.AbstractRequestDto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReviewRequestDto extends AbstractRequestDto {
    @Positive
    private Long userId;
    @Positive
    private Long bookId;
    @Positive
    private int rating;
    @NotBlank
    private String comment;
    public ReviewRequestDto() {
    }
    public ReviewRequestDto(Long userId, Long bookId, int rating, String comment) {
        this.userId = userId;
        this.bookId = bookId;
        this.rating = rating;
        this.comment = comment;
    }
}
