package com.ecommerce.book_store.service.abstraction;

import com.ecommerce.book_store.http.dto.request.implement.FcmTokenRequestDto;
import com.ecommerce.book_store.http.dto.response.implement.FcmTokenResponseDto;
import com.ecommerce.book_store.persistent.entity.FcmToken;

import java.util.List;

public interface FcmTokenService extends IService<FcmTokenRequestDto, FcmTokenResponseDto, FcmToken> {
    List<FcmToken> findByUserId(Long userId);
    List<String> findTokenStringByUserId(Long userId);
    FcmToken findByToken(String token);
    void deleteByToken(String token);
}
