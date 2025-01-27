package com.ecommerce.book_store.service.implement;

import com.ecommerce.book_store.persistent.repository.abstraction.AdminRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AdminDetailsService implements UserDetailsService {
    private final AdminRepository adminRepository;

    public AdminDetailsService(AdminRepository adminRepository) {
        this.adminRepository = adminRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        com.ecommerce.book_store.persistent.entity.Admin admin = adminRepository.findByEmail(email);
        System.out.println(email);
        System.out.println(admin);
        if (admin == null) {
            throw new UsernameNotFoundException("Admin not found");
        }

        return org.springframework.security.core.userdetails.User.builder()
                .username(admin.getEmail())
                .password(admin.getPassword())
                .disabled(false)
                .accountExpired(false)
                .accountLocked(false)
                .build();
    }
}
