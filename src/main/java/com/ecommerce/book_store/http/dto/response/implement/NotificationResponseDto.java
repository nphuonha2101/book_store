package com.ecommerce.book_store.http.dto.response.implement;

import com.ecommerce.book_store.http.dto.response.AbstractResponseDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class NotificationResponseDto extends AbstractResponseDto {
    private Long id;
    private String title;
    private String content;
    private Boolean isRead;
    private Long userId;
    private String actionUrl;
    private LocalDateTime createdAt;
}
