package com.ecommerce.book_store.persistent.repository.abstraction;

import com.ecommerce.book_store.core.constant.OrderStatus;
import com.ecommerce.book_store.persistent.entity.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.NonNullApi;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

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

    @Query("""
            SELECT COALESCE(SUM(o.totalAmount), 0) AS totalAmount
            FROM Order o
            WHERE (:month = 0 OR FUNCTION('month', o.createdAt) = :month)
            AND o.status = :status
            """)
    Map<String, Double> getOrderStats(@Param("status") OrderStatus status, @Param("month") int month);

    @Query("SELECT o FROM Order o ORDER BY o.createdAt DESC")
    Page<Order> findAllOrderByCreatedAtDesc(Pageable pageable);

    @Query("SELECT o FROM Order o ORDER BY o.createdAt DESC")
    Page<Order> findAll(Pageable pageable);
}
