package com.ecommerce.book_store.persistent.repository.abstraction;

import com.ecommerce.book_store.persistent.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    public User findByEmail(String email);
}
