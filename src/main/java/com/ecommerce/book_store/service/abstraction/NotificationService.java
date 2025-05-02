package com.ecommerce.book_store.service.abstraction;


import com.ecommerce.book_store.http.dto.request.implement.NotificationRequestDto;
import com.ecommerce.book_store.http.dto.response.implement.NotificationResponseDto;
import com.ecommerce.book_store.persistent.entity.Notification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface NotificationService extends IService<NotificationRequestDto, NotificationResponseDto, Notification> {
    void sendNotificationToUser(String title, String content, Long userId, String actionUrl) throws Exception;
    void markAsRead(Long notificationId);
    void deleteNotification(Long notificationId);
    Page<NotificationResponseDto> findByUserId(Long userId, Pageable pageable);
}
