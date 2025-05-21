package com.ecommerce.book_store.persistent.seeder;

import com.ecommerce.book_store.persistent.entity.Admin;
import com.ecommerce.book_store.persistent.repository.abstraction.AdminRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

//@Component
public class AdminLoader implements CommandLineRunner {
    private final AdminRepository adminRepository;

    public AdminLoader(AdminRepository adminRepository) {
        this.adminRepository = adminRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        Admin admin = new Admin();
        admin.setEmail("21130488@st.hcmuaf.edu.vn");
        admin.setPassword(new BCryptPasswordEncoder().encode("123456Admin"));
        admin.setName("Admin");
        adminRepository.save(admin);
    }
}
