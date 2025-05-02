package com.ecommerce.book_store.service.implement;

import com.ecommerce.book_store.http.dto.request.implement.NotificationRequestDto;
import com.ecommerce.book_store.http.dto.response.implement.NotificationResponseDto;
import com.ecommerce.book_store.persistent.entity.AbstractEntity;
import com.ecommerce.book_store.persistent.entity.Notification;
import com.ecommerce.book_store.persistent.repository.abstraction.NotificationRepository;
import com.ecommerce.book_store.service.abstraction.FcmTokenService;
import com.ecommerce.book_store.service.abstraction.NotificationService;
import com.google.firebase.messaging.BatchResponse;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.MulticastMessage;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class NotificationServiceImpl extends IServiceImpl<NotificationRequestDto, NotificationResponseDto, Notification>
        implements NotificationService {
    private final FcmTokenService fcmTokenService;

    public NotificationServiceImpl(NotificationRepository repository, FcmTokenService fcmTokenService) {
        super(repository);
        this.fcmTokenService = fcmTokenService;
    }


    @Override
    @Async
    public void sendNotificationToUser(String title, String content, Long userId, String actionUrl) {
        try {
            log.info("Sending notification to user with id: {}", userId);
            List<String> fcmTokens = fcmTokenService.findTokenStringByUserId(userId);
            log.info("Found {} tokens for user with id: {}", fcmTokens.size(), userId);

            com.google.firebase.messaging.Notification notification = com.google.firebase.messaging.Notification.builder()
                    .setTitle(title)
                    .setBody(content)
                    .build();

            // Tạo message với danh sách tokens
            MulticastMessage message = MulticastMessage.builder()
                    .addAllTokens(fcmTokens)
                    .setNotification(notification)
                    .putData("actionUrl", actionUrl)
                    .build();

            // Gửi message và nhận phản hồi
            BatchResponse response = FirebaseMessaging.getInstance().sendEachForMulticast(message);

            // Kiểm tra kết quả gửi
            int successCount = response.getSuccessCount();
            int failureCount = response.getFailureCount();

            // Ghi log kết quả gửi
            if (successCount > 0) {
                log.info("Successfully sent {} messages", successCount);
            }
            if (failureCount > 0) {
                log.error("Failed to send {} messages", failureCount);
                response.getResponses().forEach(resp -> {
                    if (!resp.isSuccessful()) {
                        log.error("Error sending message: {}", resp.getException().getMessage());
                    }
                });
            }

            // Lưu thông báo vào cơ sở dữ liệu
            Notification notificationEntity = new Notification(
                    title,
                    content,
                    false,
                    userId,
                    actionUrl
            );
            getRepository().save(notificationEntity);
        } catch (FirebaseMessagingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void markAsRead(Long notificationId) {
        Notification notification = getRepository().findById(notificationId).orElseThrow(
                () -> new RuntimeException("Notification not found")
        );

        notification.setIsRead(true);
        getRepository().save(notification);
    }

    @Override
    @Transactional
    public void deleteNotification(Long notificationId) {
        this.deleteById(notificationId);
    }

    @Override
    public Page<NotificationResponseDto> findByUserId(Long userId, Pageable pageable) {
        return ((NotificationRepository) getRepository()).findByUserId(userId, pageable)
                .map(this::toResponseDto);
    }

    @Override
    public Notification toEntity(NotificationRequestDto requestDto) {
        if (requestDto == null) {
            return null;
        }

        return new Notification(
                requestDto.getTitle(),
                requestDto.getContent(),
                requestDto.getIsRead(),
                requestDto.getUserId(),
                requestDto.getActionUrl()
        );
    }

    @Override
    public NotificationResponseDto toResponseDto(AbstractEntity entity) {
        if (entity == null) {
            return null;
        }

        Notification notification = (Notification) entity;

        return new NotificationResponseDto(
                notification.getId(),
                notification.getTitle(),
                notification.getContent(),
                notification.getIsRead(),
                notification.getUserId(),
                notification.getActionUrl(),
                notification.getCreatedAt()
        );
    }

    @Override
    public void copyProperties(NotificationRequestDto requestDto, Notification entity) {
        if (requestDto == null || entity == null) {
            return;
        }

        if (requestDto.getTitle() != null) {
            entity.setTitle(requestDto.getTitle());
        }

        if (requestDto.getContent() != null) {
            entity.setContent(requestDto.getContent());
        }

        if (requestDto.getIsRead() != null) {
            entity.setIsRead(requestDto.getIsRead());
        }

        if (requestDto.getUserId() != null) {
            entity.setUserId(requestDto.getUserId());
        }

        if (requestDto.getActionUrl() != null) {
            entity.setActionUrl(requestDto.getActionUrl());
        }

    }
}
