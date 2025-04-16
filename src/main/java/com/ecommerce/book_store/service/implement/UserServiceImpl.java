package com.ecommerce.book_store.service.implement;

import com.ecommerce.book_store.http.dto.request.implement.UserRequestDto;
import com.ecommerce.book_store.http.dto.response.implement.UserResponseDto;
import com.ecommerce.book_store.persistent.entity.AbstractEntity;
import com.ecommerce.book_store.persistent.entity.User;
import com.ecommerce.book_store.persistent.repository.abstraction.UserRepository;
import com.ecommerce.book_store.service.abstraction.MailService;
import com.ecommerce.book_store.service.abstraction.UserService;
import com.ecommerce.book_store.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.Optional;

@Service
public class UserServiceImpl extends IServiceImpl<UserRequestDto, UserResponseDto, User>
        implements UserService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final FirebaseStorageService firebaseStorageService;
    private final MailService mailService;
    @Value("${password.random.string.length}")
    private int randomPasswordLength;
    @Autowired
    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, FirebaseStorageService firebaseStorageService, MailService mailService) {
        super(userRepository);
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.firebaseStorageService = firebaseStorageService;
        this.mailService = mailService;
    }

    @Override
    public User toEntity(UserRequestDto requestDto) {
        return new User(
                requestDto.getName(),
                requestDto.getEmail(),
                passwordEncoder.encode(requestDto.getPassword()),
                requestDto.getPhone(),
                requestDto.getAddress()
        );
    }

    @Override
    public UserResponseDto toResponseDto(AbstractEntity entity) {
        if (entity == null) {
            return null;
        }

        User user = (User) entity;
        return new UserResponseDto(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getPhone(),
                user.getAddress(),
                user.getAvatar()
        );
    }

    @Override
    public void copyProperties(UserRequestDto requestDto, User entity) {
        if (requestDto.getName() != null && !requestDto.getName().isEmpty()) {
            entity.setName(requestDto.getName());
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
    @Transactional
    public UserResponseDto registerUser(UserRequestDto userDto) {
        // Check if user already exists
        if (userRepository.findByEmail(userDto.getEmail()).isPresent()) {
            throw new IllegalArgumentException("User already exists");
        }
        return this.save(userDto);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public Optional<Long> findIdByEmail(String email) {
        return userRepository.findIdByEmail(email);
    }

    @Override
    public boolean existsByUserId(Long userId) {
        return userRepository.existsById(userId);
    }


    @Override
    public boolean forgotPassword(String email) {
        Optional<User> userOptional = userRepository.findByEmail(email);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            String newPassword = StringUtil.generateRandomPassword(randomPasswordLength);
            user.setPassword(passwordEncoder.encode(newPassword));
            userRepository.save(user);

//            mailService.sendEmail(user.getEmail(), "Mật khẩu của bạn đã được thay đổi", "Mật khẩu của bạn là: " + newPassword);
            mailService.sendEmailWithTemplate(user.getEmail(), "Mật khẩu của bạn đã được thay đổi", "mails/reset-password-mail", Map.of("username", user.getName(), "newPassword", newPassword));
            return true;
        }
        return false;
    }
}
