package com.ecommerce.book_store.service.abstraction;

import com.ecommerce.book_store.core.constant.OrderStatus;
import com.ecommerce.book_store.http.dto.request.implement.OrderRequestDto;
import com.ecommerce.book_store.http.dto.response.implement.OrderResponseDto;
import com.ecommerce.book_store.persistent.entity.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public interface OrderService extends IService<OrderRequestDto, OrderResponseDto, Order> {
    OrderResponseDto order(OrderRequestDto orderRequestDto) throws Exception;
    void updateOrderStatus(Long orderId, OrderStatus newStatus);
    List<OrderStatus> getAvailableStatuses(Long orderId);
    Page<OrderResponseDto> getOrderHistory(Long userId, OrderStatus status, Pageable pageable);
    List<Order> findOrdersCreatedExactly24HoursAgo(OrderStatus status, LocalDateTime minTime, LocalDateTime maxTime);
    OrderResponseDto cancelOrder(Long orderId, String cancellationReason) throws Exception;
    Map<String, Double> getOrderStats(OrderStatus status, int month);
    OrderResponseDto findByIdAndUserId(Long id, Long userId) throws Exception;
}

