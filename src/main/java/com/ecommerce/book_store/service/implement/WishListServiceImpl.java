package com.ecommerce.book_store.service.implement;

import com.ecommerce.book_store.http.dto.request.implement.WishListRequestDto;
import com.ecommerce.book_store.http.dto.response.implement.WishListResponseDto;
import com.ecommerce.book_store.persistent.entity.AbstractEntity;
import com.ecommerce.book_store.persistent.entity.Book;
import com.ecommerce.book_store.persistent.entity.WishList;
import com.ecommerce.book_store.persistent.repository.abstraction.WishListRepository;
import com.ecommerce.book_store.service.abstraction.BookService;
import com.ecommerce.book_store.service.abstraction.UserService;
import com.ecommerce.book_store.service.abstraction.WishListService;
import org.springframework.boot.autoconfigure.liquibase.LiquibaseProperties;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WishListServiceImpl  extends IServiceImpl<WishListRequestDto, WishListResponseDto, WishList>
        implements WishListService {
    private final UserService userService;
    private final BookService bookService;
    private final WishListRepository wishListRepository;


    public WishListServiceImpl(UserService userService, BookService bookService, WishListRepository wishListRepository) {
        super(wishListRepository);
        this.userService = userService;
        this.bookService = bookService;
        this.wishListRepository = wishListRepository;
    }
    @Override
    public WishList addBookToWishList(Long userId, Long bookId) {
        if (wishListRepository.existsByUserIdAndBookId(userId, bookId)) {
            throw new RuntimeException("Book already in wish list");
        }
        Book book = bookService.findById(bookId);
        WishList wishList = new WishList(userId, book);
        return wishListRepository.save(wishList);
    }
    @Override
    public void removeBookFromWishList(Long userId, Long bookId) {
        if (!wishListRepository.existsByUserIdAndBookId(userId, bookId)) {
            throw new RuntimeException("Book not found in wish list");
        }
        wishListRepository.deleteByUserIdAndBookId(userId, bookId);

    }

    @Override
    public List<WishList> getWishListByUserId(Long userId) {
        return wishListRepository.findByUserId(userId);
    }

    @Override
    public boolean isBookInWishList(Long userId, Long bookId) {
        return wishListRepository.existsByUserIdAndBookId(userId, bookId);
    }

    @Override
    public boolean toggleWishList(Long userId, Long bookId) {
        if (isBookInWishList(userId, bookId)) {
            removeBookFromWishList(userId, bookId);
            return false;
        } else {
            addBookToWishList(userId, bookId);
            return true;
        }
    }

    @Override
    public WishList toEntity(WishListRequestDto requestDto) {
        WishList result = new WishList(
                requestDto.getUserId(),
                null
        );
        Book book = bookService.findById(requestDto.getBookId());
        return result;
    }

    @Override
    public WishListResponseDto toResponseDto(AbstractEntity entity) {
        WishList wishList = (WishList) entity;
        return new WishListResponseDto(
                wishList.getId(),
                wishList.getUserId(),
                bookService.toResponseDto(wishList.getBook())
        );
    }

    @Override
    public void copyProperties(WishListRequestDto requestDto, WishList entity) {
        entity.setUserId(requestDto.getUserId());
        entity.setBook(bookService.findById(requestDto.getBookId()));

    }
}
