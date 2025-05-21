package com.ecommerce.book_store.http.dto.request.implement;

import com.ecommerce.book_store.http.dto.request.AbstractRequestDto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NotificationRequestDto extends AbstractRequestDto {
    private String title;
    private String content;
    private Boolean isRead;
    private Long userId;
    private String actionUrl;
}
