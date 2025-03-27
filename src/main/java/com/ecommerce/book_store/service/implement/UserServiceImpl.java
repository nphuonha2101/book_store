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
    private final FirebaseStorageService firebaseStorageService;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, FirebaseStorageService firebaseStorageService) {
        super(userRepository);
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.firebaseStorageService = firebaseStorageService;
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
        if (requestDto.getUsername() != null && !requestDto.getUsername().isBlank()) {
            entity.setName(requestDto.getUsername());
        }

        if (requestDto.getEmail() != null && !requestDto.getEmail().isBlank()) {
            entity.setEmail(requestDto.getEmail());
        }

        if (requestDto.getPassword() != null && !requestDto.getPassword().isBlank()) {
            entity.setPassword(passwordEncoder.encode(requestDto.getPassword()));
        }

        if (requestDto.getPhone() != null && !requestDto.getPhone().isEmpty()) {
            entity.setPhone(requestDto.getPhone());
        }

        if (requestDto.getAddress() != null && !requestDto.getAddress().isBlank()) {
            entity.setAddress(requestDto.getAddress());
        }

        if (requestDto.getAvatar() != null && !requestDto.getAvatar().isEmpty()) {
            String imageUrl = firebaseStorageService.uploadFile(requestDto.getAvatar(), "users/avatar");
            entity.setAvatar(imageUrl);
        }
    }

    @Override
    public User registerUser(UserRequestDto userDto) {
        // Check if user already exists
        if (userRepository.findByEmail(userDto.getEmail()).isPresent()) {
            throw new RuntimeException("User already exists");
        }

        // Create new user
        User user = new User();
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

    @Override
    public Optional<Long> findIdByEmail(String email) {
        return userRepository.findIdByEmail(email);
    }
}
