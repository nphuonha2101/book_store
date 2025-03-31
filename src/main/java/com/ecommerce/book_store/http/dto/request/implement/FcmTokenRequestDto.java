package com.ecommerce.book_store.http.dto.request.implement;

import com.ecommerce.book_store.http.dto.request.AbstractRequestDto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FcmTokenRequestDto extends AbstractRequestDto {
    private String token;
    private Long userId;
}
