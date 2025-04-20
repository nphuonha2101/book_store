package com.ecommerce.book_store.service.abstraction;

import com.ecommerce.book_store.http.dto.request.implement.OrderItemRequestDto;
import com.ecommerce.book_store.http.dto.response.implement.OrderItemResponseDto;
import com.ecommerce.book_store.persistent.entity.OrderItem;

public interface OrderItemService extends IService<OrderItemRequestDto, OrderItemResponseDto, OrderItem> {
}
