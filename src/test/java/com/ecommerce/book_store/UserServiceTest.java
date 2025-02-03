package com.ecommerce.book_store;

import com.ecommerce.book_store.http.dto.request.implement.UserRequestDto;
import com.ecommerce.book_store.persistent.entity.User;
import com.ecommerce.book_store.service.abstraction.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootTest
public class UserServiceTest {
    @Autowired
    private UserService userService;
    @Test
    public void testRegisterUser() {
        UserRequestDto userRequestDto = new UserRequestDto();
//        userRequestDto.setUsername("user");
        userRequestDto.setEmail("21130488@st.hcmuaf.edu.vn");
        userRequestDto.setPassword(new BCryptPasswordEncoder().encode("123456"));
        userRequestDto.setAvatar("https://i.pravatar.cc/150?img=68");
        userRequestDto.setPhone("0123456789");
        userRequestDto.setAddress("HCM");
        userRequestDto.setRoleId(1L);

        assert userService.registerUser(userRequestDto) != null;
    }
}
