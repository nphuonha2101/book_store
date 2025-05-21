package com.ecommerce.book_store.service.implement;

import com.ecommerce.book_store.http.dto.request.implement.FcmTokenRequestDto;
import com.ecommerce.book_store.http.dto.response.implement.FcmTokenResponseDto;
import com.ecommerce.book_store.persistent.entity.AbstractEntity;
import com.ecommerce.book_store.persistent.entity.FcmToken;
import com.ecommerce.book_store.persistent.repository.abstraction.FcmTokenRepository;
import com.ecommerce.book_store.service.abstraction.FcmTokenService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class FcmTokenServiceImpl extends IServiceImpl<FcmTokenRequestDto, FcmTokenResponseDto, FcmToken> implements FcmTokenService {
    private final FcmTokenRepository fcmTokenRepository;

    public FcmTokenServiceImpl(FcmTokenRepository fcmTokenRepository) {
        super(fcmTokenRepository);
        this.fcmTokenRepository = fcmTokenRepository;
    }

    @Override
    public List<FcmToken> findByUserId(Long userId) {
        return fcmTokenRepository.findByUserId(userId);
    }

    @Override
    public List<String> findTokenStringByUserId(Long userId) {
        return fcmTokenRepository.findTokenStringByUserId(userId);
    }

    @Override
    public FcmToken findByToken(String token) {
        return fcmTokenRepository.findByToken(token);
    }

    @Override
    @Transactional
    public void deleteByToken(String token) {
        fcmTokenRepository.deleteByToken(token);
    }

    @Override
    public FcmToken toEntity(FcmTokenRequestDto requestDto) {
        return new FcmToken(
                requestDto.getToken(),
                requestDto.getUserId()
        );
    }

    @Override
    public FcmTokenResponseDto toResponseDto(AbstractEntity entity) {
        return null;
    }

    @Override
    public void copyProperties(FcmTokenRequestDto requestDto, FcmToken entity) {
        if (requestDto.getToken() != null && !requestDto.getToken().isBlank()) {
            entity.setToken(requestDto.getToken());
        }
        if (requestDto.getUserId() != null) {
            entity.setUserId(requestDto.getUserId());
        }
    }
}
