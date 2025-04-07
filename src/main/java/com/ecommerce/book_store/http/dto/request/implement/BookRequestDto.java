package com.ecommerce.book_store.http.dto.request.implement;

import com.ecommerce.book_store.http.dto.request.AbstractRequestDto;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class BookRequestDto extends AbstractRequestDto {
    private String title;
    private String authorName;
    private String description;
    private String isbn;
    private Long categoryId;
    private MultipartFile coverImage;
    private List<MultipartFile> bookImages;
    private Integer price;
    private Integer quantity;
    private boolean isAvailable;
    private LocalDateTime publishedAt;
}
