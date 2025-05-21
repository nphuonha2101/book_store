package com.ecommerce.book_store.persistent.repository.abstraction;

import com.ecommerce.book_store.core.constant.OrderStatus;
import com.ecommerce.book_store.persistent.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    Order findByIdAndUserId(Long orderId, Long userId);
    @Query("SELECT o FROM Order o WHERE o.user.id = ?1 AND o.status = ?2 ORDER BY o.createdAt DESC")
    List<Order> findAllByUserIdAndStatus(Long userId, OrderStatus status);
    @Query("SELECT o FROM Order o WHERE o.user.id = ?1 ORDER BY o.createdAt DESC")
    List<Order> findAllByUserId(Long userId);
    @Query("SELECT o FROM Order o WHERE o.status = :status AND o.createdAt <= :maxTime AND o.createdAt > :minTime")
    List<Order> findOrdersCreatedExactly24HoursAgo(
            @Param("status") OrderStatus status,
            @Param("minTime") LocalDateTime minTime,
            @Param("maxTime") LocalDateTime maxTime
    );

}
