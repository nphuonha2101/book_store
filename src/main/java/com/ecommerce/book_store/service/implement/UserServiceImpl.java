package com.ecommerce.book_store.service.implement;

import com.ecommerce.book_store.http.dto.request.implement.UserRequestDto;
import com.ecommerce.book_store.http.dto.response.implement.UserResponseDto;
import com.ecommerce.book_store.persistent.entity.AbstractEntity;
import com.ecommerce.book_store.persistent.entity.User;
import com.ecommerce.book_store.persistent.repository.abstraction.UserRepository;
import com.ecommerce.book_store.service.abstraction.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl extends IServiceImpl<UserRequestDto, UserResponseDto, User>
            implements UserService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        super(userRepository);
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User toEntity(UserRequestDto requestDto) {
        return new User(
                requestDto.getUsername(),
                requestDto.getEmail(),
                requestDto.getPassword(),
                requestDto.getPhone(),
                requestDto.getAddress()
        );
    }

    @Override
    public UserResponseDto toResponseDto(AbstractEntity entity) {
        return null;
    }

    @Override
    public void copyProperties(UserRequestDto requestDto, User entity) {

    }

    @Override
    public User registerUser(UserRequestDto userDto) {
        // Check if user already exists
        if (userRepository.findByEmail(userDto.getEmail()) != null) {
            throw new RuntimeException("User already exists");
        }

        // Create new user
        User user = new User();
//        user.setName(userDto.getUsername());
        user.setEmail(userDto.getEmail());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        user.setPhone(userDto.getPhone());
        user.setAddress(userDto.getAddress());

        return userRepository.save(user);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }
}
