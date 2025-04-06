package com.ecommerce.book_store.service.abstraction;

import com.ecommerce.book_store.http.dto.request.implement.UserRequestDto;
import com.ecommerce.book_store.http.dto.response.implement.UserResponseDto;
import com.ecommerce.book_store.persistent.entity.User;

import java.util.Optional;

public interface UserService extends IService<UserRequestDto, UserResponseDto, User> {
    UserResponseDto registerUser(UserRequestDto userRequestDto);
    Optional<User> findByEmail(String email);
    Optional<Long> findIdByEmail(String email);
    boolean existsByUserId(Long userId);
}
