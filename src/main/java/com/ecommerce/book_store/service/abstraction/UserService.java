package com.ecommerce.book_store.service.abstraction;

import com.ecommerce.book_store.http.dto.request.implement.UserRequestDto;
import com.ecommerce.book_store.http.dto.response.implement.UserResponseDto;
import com.ecommerce.book_store.persistent.entity.User;

public interface UserService extends IService<UserRequestDto, UserResponseDto, User> {
    User registerUser(UserRequestDto userRequestDto);
}
