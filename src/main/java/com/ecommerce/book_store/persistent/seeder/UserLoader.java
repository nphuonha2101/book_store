package com.ecommerce.book_store.persistent.seeder;

import com.ecommerce.book_store.persistent.entity.User;
import com.ecommerce.book_store.persistent.repository.abstraction.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

//@Component
public class UserLoader implements CommandLineRunner {
    private final UserRepository userRepository;

    public UserLoader(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        User user = new User();
        user.setName("user");
        user.setEmail("21130122@st.hcmuaf.edu.vn");
        user.setPassword(new BCryptPasswordEncoder().encode("123456"));
        user.setAvatar("https://i.pravatar.cc/150?img=68");
        user.setPhone("0123456789");
        user.setAddress("HCM");
        userRepository.save(user);
    }
}
