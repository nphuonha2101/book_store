package com.ecommerce.book_store.service.implement;

import com.ecommerce.book_store.http.dto.request.implement.UserRequestDto;
import com.ecommerce.book_store.http.dto.response.implement.UserResponseDto;
import com.ecommerce.book_store.persistent.entity.AbstractEntity;
import com.ecommerce.book_store.persistent.entity.User;
import com.ecommerce.book_store.persistent.repository.abstraction.UserRepository;
import com.ecommerce.book_store.service.abstraction.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends IServiceImpl<UserRequestDto, UserResponseDto, User>
            implements UserService {
    private final RoleServiceImpl roleService;
    @Autowired
    public UserServiceImpl(UserRepository userRepository, RoleServiceImpl roleService) {
        super(userRepository);
        this.roleService = roleService;
    }

    @Override
    public User toEntity(UserRequestDto requestDto) {
        return null;
    }

    @Override
    public UserResponseDto toResponseDto(AbstractEntity entity) {
        return null;
    }

    @Override
    public void copyProperties(UserRequestDto requestDto, User entity) {

    }
}
