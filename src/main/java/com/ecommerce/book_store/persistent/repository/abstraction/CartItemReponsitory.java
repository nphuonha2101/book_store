package com.ecommerce.book_store.persistent.repository.abstraction;

import com.ecommerce.book_store.persistent.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartItemReponsitory extends JpaRepository<CartItem, Long> {
    List<CartItem> findByUserId(Long userId);
}
