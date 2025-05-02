package com.ecommerce.book_store.persistent.repository.abstraction;

import com.ecommerce.book_store.persistent.entity.FcmToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface FcmTokenRepository extends JpaRepository<FcmToken, Long> {
    List<FcmToken> findByUserId(Long userId);
    @Query("SELECT f.token FROM FcmToken f WHERE f.userId = ?1")
    List<String> findTokenStringByUserId(Long userId);
    FcmToken findByToken(String token);
    void deleteByToken(String token);
}
