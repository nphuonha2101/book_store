package com.ecommerce.book_store.service.abstraction;

import com.ecommerce.book_store.http.dto.request.implement.WishListRequestDto;
import com.ecommerce.book_store.http.dto.response.implement.WishListResponseDto;
import com.ecommerce.book_store.persistent.entity.WishList;

import java.util.List;

public interface WishListService extends IService<WishListRequestDto, WishListResponseDto, WishList> {
    WishList addBookToWishList(Long userId, Long bookId);
    void removeBookFromWishList(Long userId, Long bookId);
    List<WishList> getWishListByUserId(Long userId);
    boolean isBookInWishList(Long userId, Long bookId);
    boolean toggleWishList(Long userId, Long bookId);
}
