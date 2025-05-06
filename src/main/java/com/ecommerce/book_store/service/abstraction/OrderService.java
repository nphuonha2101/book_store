package com.ecommerce.book_store.service.abstraction;

import com.ecommerce.book_store.core.constant.OrderStatus;
import com.ecommerce.book_store.http.dto.request.implement.OrderRequestDto;
import com.ecommerce.book_store.http.dto.response.implement.OrderResponseDto;
import com.ecommerce.book_store.persistent.entity.Order;

import java.time.LocalDateTime;
import java.util.List;

public interface OrderService extends IService<OrderRequestDto, OrderResponseDto, Order> {
    OrderResponseDto order(OrderRequestDto orderRequestDto) throws Exception;
    void updateOrderStatus(Long orderId, OrderStatus newStatus);
    List<OrderStatus> getAvailableStatuses(Long orderId);
    List<OrderResponseDto> getOrderHistory(Long userId, OrderStatus status);
    List<Order> findOrdersCreatedExactly24HoursAgo(OrderStatus status, LocalDateTime minTime, LocalDateTime maxTime);
    OrderResponseDto cancelOrder(Long orderId, String cancellationReason) throws Exception;
}

