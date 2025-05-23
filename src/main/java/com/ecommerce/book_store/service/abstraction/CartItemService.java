package com.ecommerce.book_store.service.abstraction;

import com.ecommerce.book_store.http.dto.request.implement.CartItemRequestDto;
import com.ecommerce.book_store.http.dto.response.implement.CartItemResponseDto;
import com.ecommerce.book_store.persistent.entity.CartItem;

import java.util.List;
import java.util.Optional;

public interface CartItemService extends IService<CartItemRequestDto, CartItemResponseDto, CartItem>
{
    List<CartItemResponseDto> getCartItemsByUserId(Long userId);
    CartItemResponseDto addCartItem(Long userId, Long bookId, int quantity);
    CartItemResponseDto updateCartItem(Long cartItemId, int quantity);
    void deleteCartItem(Long cartItemId);
    void deleteAllCartItems(Long userId);
}
