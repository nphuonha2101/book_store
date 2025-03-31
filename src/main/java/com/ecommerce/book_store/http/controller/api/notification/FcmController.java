package com.ecommerce.book_store.http.controller.api.notification;

import com.ecommerce.book_store.http.ApiResponse;
import com.ecommerce.book_store.http.dto.request.implement.FcmTokenRequestDto;
import com.ecommerce.book_store.persistent.entity.FcmToken;
import com.ecommerce.book_store.service.abstraction.FcmTokenService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth/fcm")
public class FcmController {
    private final FcmTokenService fcmService;

    public FcmController(FcmTokenService fcmService) {
        this.fcmService = fcmService;
    }

    @PostMapping("/token")
    public ResponseEntity<ApiResponse<FcmToken>> saveToken(@RequestBody FcmTokenRequestDto requestDto) {
        try {
            System.out.println(requestDto.getToken());
            FcmToken fcmToken = fcmService.save(requestDto);
            return fcmToken != null ? ApiResponse.success(fcmToken, "Fcm token đã được lưu") : ApiResponse.error("Lưu fcm token thất bại", HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/tokens")
    public ResponseEntity<ApiResponse<Iterable<FcmToken>>> getTokens(@RequestParam(required = false) Long userId) {
        try {
            Iterable<FcmToken> fcmTokens = fcmService.findByUserId(userId);
            return ApiResponse.success(fcmTokens, "Danh sách fcm token");
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/token")
    public ResponseEntity<ApiResponse<String>> deleteToken(@RequestParam String token) {
        try {
            fcmService.deleteByToken(token);
            return ApiResponse.success(null, "Xóa fcm token thành công");
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
