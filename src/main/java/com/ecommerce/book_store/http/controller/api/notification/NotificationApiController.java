package com.ecommerce.book_store.http.controller.api.notification;

import com.ecommerce.book_store.core.security.JwtUtils;
import com.ecommerce.book_store.http.ApiResponse;
import com.ecommerce.book_store.http.dto.response.implement.NotificationResponseDto;
import com.ecommerce.book_store.service.abstraction.NotificationService;
import com.ecommerce.book_store.service.abstraction.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/auth/notifications")
public class NotificationApiController {
    private final NotificationService notificationService;
    private final JwtUtils jwtUtils;
    private final UserService userService;

    public NotificationApiController(NotificationService notificationService, JwtUtils jwtUtils, UserService userService) {
        this.notificationService = notificationService;
        this.jwtUtils = jwtUtils;
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<NotificationResponseDto>>> getNotifications(
            @RequestHeader("Authorization") String token,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        try {
            String jwtToken = token.substring(7);
            String email = jwtUtils.extractUserEmail(jwtToken);
            Long userId = userService.findIdByEmail(email).orElseThrow(
                    () -> new RuntimeException("User not found")
            );

            Page<NotificationResponseDto> notifications = notificationService.findByUserId(userId, PageRequest.of(page, size));

            return ApiResponse.successWithPagination(notifications, "Tất cả sách được lấy thành công");

        } catch (Exception e) {
            return ApiResponse.error("Có lỗi xảy ra", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/unread/count")
    public ResponseEntity<ApiResponse<Integer>> countUnreadNotifications(
            @RequestHeader("Authorization") String token
    ) {
        try {
            String jwtToken = token.substring(7);
            String email = jwtUtils.extractUserEmail(jwtToken);
            Long userId = userService.findIdByEmail(email).orElseThrow(
                    () -> new RuntimeException("User not found")
            );

            Integer count = notificationService.countUnreadNotificationsByUserId(userId);

            return ApiResponse.success(count, "Số lượng thông báo chưa đọc");

        } catch (Exception e) {
            return ApiResponse.error("Có lỗi xảy ra", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/mark-as-read/{notificationId}")
    public ResponseEntity<ApiResponse<Void>> markAsRead(
            @PathVariable Long notificationId
    ) {
        try {
            notificationService.markAsRead(notificationId);

            return ApiResponse.success(null, "Đánh dấu thông báo là đã đọc thành công");

        } catch (Exception e) {
            log.error("NotificationApiController.markAsRead: {}", e.getMessage());
            return ApiResponse.error("Có lỗi xảy ra", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/mark-as-read-all")
    public ResponseEntity<ApiResponse<Boolean>> markAsReadAll(
            @RequestHeader("Authorization") String token
    ) {
        try {
            String jwtToken = token.substring(7);
            String email = jwtUtils.extractUserEmail(jwtToken);
            Long userId = userService.findIdByEmail(email).orElseThrow(
                    () -> new RuntimeException("User not found")
            );

            boolean result = notificationService.markAsReadAllByUserId(userId);

            return ApiResponse.success(result, "Đánh dấu tất cả thông báo là đã đọc thành công");

        } catch (Exception e) {
            log.error("NotificationApiController.markAsReadAll: {}", e.getMessage());
            return ApiResponse.error("Có lỗi xảy ra", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
