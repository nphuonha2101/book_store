package com.ecommerce.book_store.service.abstraction;

import com.ecommerce.book_store.http.dto.request.implement.OrderRequestDto;
import com.ecommerce.book_store.http.dto.response.implement.OrderResponseDto;
import com.ecommerce.book_store.persistent.entity.Order;

public interface OrderService extends IService<OrderRequestDto, OrderResponseDto, Order> {
    public OrderResponseDto order(OrderRequestDto orderRequestDto) throws Exception;
}

