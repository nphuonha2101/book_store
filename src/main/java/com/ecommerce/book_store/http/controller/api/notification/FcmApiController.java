package com.ecommerce.book_store.http.controller.api.notification;

import com.ecommerce.book_store.core.security.JwtUtils;
import com.ecommerce.book_store.http.ApiResponse;
import com.ecommerce.book_store.http.dto.request.implement.FcmTokenRequestDto;
import com.ecommerce.book_store.http.dto.response.implement.FcmTokenResponseDto;
import com.ecommerce.book_store.persistent.entity.FcmToken;
import com.ecommerce.book_store.service.abstraction.FcmTokenService;
import com.ecommerce.book_store.service.abstraction.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/v1/auth/fcm")
public class FcmApiController {
    private final FcmTokenService fcmService;
    private final UserService userService;
    private final JwtUtils jwtUtils;

    public FcmApiController(FcmTokenService fcmService, UserService userService, JwtUtils jwtUtils) {
        this.fcmService = fcmService;
        this.userService = userService;
        this.jwtUtils = jwtUtils;
    }

    @PostMapping("/token")
    public ResponseEntity<ApiResponse<FcmTokenResponseDto>> saveToken(@RequestBody FcmTokenRequestDto requestDto,
                                                           @RequestHeader("Authorization") String token) {
        try {
            String jwtToken = token.substring(7);
            String email = jwtUtils.extractUserEmail(jwtToken);
            Long userId = userService.findIdByEmail(email).orElseThrow(
                    () -> new RuntimeException("User not found")
            );
            requestDto.setUserId(userId);

            FcmTokenResponseDto fcmToken = fcmService.save(requestDto);
            return ApiResponse.success(fcmToken, "Fcm token đã được lưu");
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/tokens")
    public ResponseEntity<ApiResponse<Iterable<FcmToken>>> getTokens(@RequestHeader("Authorization") String token) {
        try {
            String jwtToken = token.substring(7);
            String email = jwtUtils.extractUserEmail(jwtToken);
            Long userId = userService.findIdByEmail(email).orElseThrow(
                    () -> new RuntimeException("User not found")
            );

            Iterable<FcmToken> fcmTokens = fcmService.findByUserId(userId);
            return ApiResponse.success(fcmTokens, "Danh sách fcm token");
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<ApiResponse<String>> deleteToken(@RequestBody  String token) {
        try {
            log.info("Token received: {}", token);
            fcmService.deleteByToken(token);
            return ApiResponse.success(null, "Xóa fcm token thành công");
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
