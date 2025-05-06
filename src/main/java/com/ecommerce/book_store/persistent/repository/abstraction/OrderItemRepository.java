package com.ecommerce.book_store.persistent.repository.abstraction;

import com.ecommerce.book_store.persistent.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
    @Query("SELECT COUNT(oi) > 0 FROM OrderItem oi " +
            "JOIN oi.order o " +
            "WHERE o.user.id = :userId AND oi.book.id = :bookId AND o.status = com.ecommerce.book_store.core.constant.OrderStatus.DELIVERED")
    boolean isUserPurchasedBook(@Param("userId") Long userId, @Param("bookId") Long bookId);
}
