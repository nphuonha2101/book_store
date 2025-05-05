package com.ecommerce.book_store.persistent.repository.abstraction;

import com.ecommerce.book_store.core.constant.OrderStatus;
import com.ecommerce.book_store.persistent.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    Order findByIdAndUserId(Long orderId, Long userId);
    List<Order> findAllByUserIdAndStatus(Long userId, OrderStatus status);
    List<Order> findAllByUserId(Long userId);
}
