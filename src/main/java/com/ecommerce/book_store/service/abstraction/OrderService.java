package com.ecommerce.book_store.service.abstraction;

import com.ecommerce.book_store.core.constant.OrderStatus;
import com.ecommerce.book_store.http.dto.request.implement.OrderRequestDto;
import com.ecommerce.book_store.http.dto.response.implement.OrderResponseDto;
import com.ecommerce.book_store.persistent.entity.Order;

import java.util.List;

public interface OrderService extends IService<OrderRequestDto, OrderResponseDto, Order> {
    OrderResponseDto order(OrderRequestDto orderRequestDto) throws Exception;
    void updateOrderStatus(Long orderId, OrderStatus newStatus);
    List<OrderStatus> getAvailableStatuses(Long orderId);
}

