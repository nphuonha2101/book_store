package com.ecommerce.book_store.service.abstraction;

import com.ecommerce.book_store.http.dto.request.implement.CartItemRequestDto;
import com.ecommerce.book_store.http.dto.response.implement.CartItemResponseDto;
import com.ecommerce.book_store.persistent.entity.CartItem;

import java.util.List;
import java.util.Optional;

public interface CartItemService extends IService<CartItemRequestDto, CartItemResponseDto, CartItem>
{
    List<CartItem> getCartItemsByUserId(Long userId);
    CartItem addCartItem(Long userId, Long bookId, int quantity);
    CartItem updateCartItem(Long userId, Long bookId, int quantity);
    void deleteCartItem(Long userId, Long bookId);
}
