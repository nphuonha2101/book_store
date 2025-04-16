package com.ecommerce.book_store.http.controller.api.auth;

import com.ecommerce.book_store.core.security.JwtUtils;
import com.ecommerce.book_store.http.ApiResponse;
import com.ecommerce.book_store.http.dto.request.implement.AuthRequest;
import com.ecommerce.book_store.http.dto.request.implement.UserRequestDto;
import com.ecommerce.book_store.http.dto.response.implement.UserResponseDto;
import com.ecommerce.book_store.persistent.entity.User;
import com.ecommerce.book_store.service.abstraction.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
public class AuthApiController {
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final UserService userService;

    public AuthApiController(AuthenticationManager authenticationManager, JwtUtils jwtService, UserService userService) {
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtService;
        this.userService = userService;
    }

    @PostMapping("/api/v1/login")
    public ResponseEntity<ApiResponse<String>> login(@RequestBody AuthRequest request) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
            );
            if (authentication == null) {
                return ApiResponse.error("Không xác thực", HttpStatus.UNAUTHORIZED);
            }

            String jwtToken = jwtUtils.generateToken(request.getEmail());

            return ApiResponse.success(jwtToken, "Đang nhập thành công");
        } catch (BadCredentialsException e) {
            return ApiResponse.error("Sai tài khoản hoặc mật khẩu", HttpStatus.UNAUTHORIZED);
        } catch (Exception e) {
            return ApiResponse.error("Có lỗi xảy ra: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/api/v1/register")
    public ResponseEntity<ApiResponse<UserResponseDto>> registerUser(
            @RequestBody UserRequestDto userRequestDto) {
        try {
            UserResponseDto user = userService.registerUser(userRequestDto);
            return user != null ? ApiResponse.success(user, "Register successfully") :
                    ApiResponse.error("Register failed", HttpStatus.BAD_REQUEST);
        } catch (RuntimeException e) {
            return ApiResponse.error(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/api/v1/auth/logout")
    public ResponseEntity<ApiResponse<String>> logout() {
        return ApiResponse.success(null, "Logout successfully");
    }

    @PostMapping("/api/v1/auth/me")
    public ResponseEntity<ApiResponse<User>> me(@RequestHeader("Authorization") String token) {
        try {
            String jwtToken = token.substring(7);
            String email = jwtUtils.extractUserEmail(jwtToken);
            Optional<User> userOptional = userService.findByEmail(email);
            return userOptional.map(user -> ApiResponse.success(user, "Get user successfully")).orElseGet(() -> ApiResponse.error("User not found", HttpStatus.NOT_FOUND));
        } catch (Exception e) {
            return ApiResponse.error("Internal Server Error: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/api/v1/auth/update")
    public ResponseEntity<ApiResponse<UserResponseDto>> update(@RequestHeader("Authorization") String token, @ModelAttribute UserRequestDto userRequestDto) {
        try {
            String jwtToken = token.substring(7);
            String email = jwtUtils.extractUserEmail(jwtToken);
            Optional<Long> userOptional = userService.findIdByEmail(email);
            if (userOptional.isEmpty()) {
                return ApiResponse.error("User not found", HttpStatus.NOT_FOUND);
            }
            Long userId = userOptional.get();
            UserResponseDto updatedUser = userService.update(userRequestDto, userId);
            return ApiResponse.success(updatedUser, "Update user successfully");
        } catch (Exception e) {
            return ApiResponse.error("Internal Server Error: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @PostMapping("/api/v1/forgot-password")
    public ResponseEntity<ApiResponse<String>> forgotPassword(@RequestBody AuthRequest request) {
        try {
            boolean forgotPasswordSuccess = userService.forgotPassword(request.getEmail());

            return forgotPasswordSuccess ? ApiResponse.success(null, "Forgot password successfully") :
                    ApiResponse.error("Forgot password failed", HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return ApiResponse.error("Internal Server Error: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
