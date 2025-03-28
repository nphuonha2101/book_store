package com.ecommerce.book_store.service.implement;

import com.ecommerce.book_store.http.dto.request.implement.CartItemRequestDto;
import com.ecommerce.book_store.http.dto.response.implement.BookResponseDto;
import com.ecommerce.book_store.http.dto.response.implement.CartItemResponseDto;
import com.ecommerce.book_store.http.dto.response.implement.UserResponseDto;
import com.ecommerce.book_store.persistent.entity.*;
import com.ecommerce.book_store.persistent.repository.abstraction.CartItemReponsitory;
import com.ecommerce.book_store.service.abstraction.BookService;
import com.ecommerce.book_store.service.abstraction.CartItemService;
import com.ecommerce.book_store.service.abstraction.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CartServiceImpl extends IServiceImpl<CartItemRequestDto, CartItemResponseDto, CartItem>
        implements CartItemService {
    private final BookService bookService;
    private final CartItemReponsitory cartItemReponsitory;

   @Autowired
   public CartServiceImpl(CartItemReponsitory cartItemReponsitory, BookService bookService) {
        super(cartItemReponsitory);
        this.bookService = bookService;
        this.cartItemReponsitory = cartItemReponsitory;
    }

    @Override
    public CartItem toEntity(CartItemRequestDto requestDto) {
        CartItem result = new CartItem(
                requestDto.getUserId(),
                null,
                requestDto.getQuantity(),
                requestDto.getPrice()
        );
        Book book = bookService.findById(requestDto.getBookId());
        return result;
    };

    @Override
    public CartItemResponseDto toResponseDto(AbstractEntity entity) {
        CartItem cartItem = (CartItem) entity;
        BookResponseDto cartBook = bookService.toResponseDto(cartItem.getBook());

        return new CartItemResponseDto(
                cartItem.getId(),
                cartItem.getUserId(),
                cartBook,
                cartItem.getQuantity(),
                cartItem.getPrice()
        );
    }

    @Override
    public void copyProperties(CartItemRequestDto requestDto, CartItem entity) {
        entity.setUserId(requestDto.getUserId());
        entity.setBook(bookService.findById(requestDto.getBookId()));
        entity.setQuantity(requestDto.getQuantity());
        entity.setPrice(requestDto.getPrice());

    }

//    @Override
//    public Optional<CartItem> findCartItem(Long userId) {
//        return cartItemReponsitory.findById(userId);
//    }

    @Override
    public List<CartItem> getCartItemsByUserId(Long userId) {
        return cartItemReponsitory.findByUserId(userId);
    }

    @Override
    public CartItem addCartItem(Long userId, Long bookId, int quantity) {
        List<CartItem> existing = cartItemReponsitory.findByUserId(userId);
        for (CartItem cartItem : existing) {
            if (cartItem.getBook().getId().equals(bookId)) {
                cartItem.setQuantity(cartItem.getQuantity() + quantity);
                return cartItemReponsitory.save(cartItem);
            }
        }
    // create new cart item if not exist
        CartItem cartItem = new CartItem();
        cartItem.setUserId(userId);
        cartItem.setBook(bookService.findById(bookId));
        cartItem.setQuantity(quantity);
        return cartItemReponsitory.save(cartItem);
    }

    @Override
    public CartItem updateCartItem(Long userId, Long bookId, int quantity) {
        List<CartItem> existing = cartItemReponsitory.findByUserId(userId);
        for (CartItem cartItem : existing) {
            if (cartItem.getBook().getId().equals(bookId)) {
                cartItem.setQuantity(quantity);
                return cartItemReponsitory.save(cartItem);
            }
        }
        return null;
    }

    @Override
    public void deleteCartItem(Long userId, Long bookId) {
        List<CartItem> existing = cartItemReponsitory.findByUserId(userId);
        for (CartItem cartItem : existing) {
            if (cartItem.getBook().getId().equals(bookId)) {
                cartItemReponsitory.delete(cartItem);
                return;
            }
        }

    }
}
