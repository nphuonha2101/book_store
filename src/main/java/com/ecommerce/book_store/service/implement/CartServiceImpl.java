package com.ecommerce.book_store.service.implement;

import com.ecommerce.book_store.http.dto.request.implement.CartItemRequestDto;
import com.ecommerce.book_store.http.dto.response.implement.BookResponseDto;
import com.ecommerce.book_store.http.dto.response.implement.CartItemResponseDto;
import com.ecommerce.book_store.persistent.entity.*;
import com.ecommerce.book_store.persistent.repository.abstraction.CartItemRepository;
import com.ecommerce.book_store.service.abstraction.BookService;
import com.ecommerce.book_store.service.abstraction.CartItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CartServiceImpl extends IServiceImpl<CartItemRequestDto, CartItemResponseDto, CartItem>
        implements CartItemService {
    private final BookService bookService;
    private final CartItemRepository cartItemRepository;

   @Autowired
   public CartServiceImpl(CartItemRepository cartItemRepository, BookService bookService) {
        super(cartItemRepository);
        this.bookService = bookService;
        this.cartItemRepository = cartItemRepository;
    }

    @Override
    public CartItem toEntity(CartItemRequestDto requestDto) {
        Book book = bookService.getRepository().findById(requestDto.getBookId()).orElseThrow(
                () -> new RuntimeException("Book not found")
        );
        return new CartItem(
                requestDto.getUserId(),
                book,
                requestDto.getQuantity(),
                requestDto.getPrice()
        );
    }

    @Override
    public CartItemResponseDto toResponseDto(AbstractEntity entity) {
        if (entity == null) {
            return null;
        }

        CartItem cartItem = (CartItem) entity;
        BookResponseDto cartBook = bookService.toResponseDto(cartItem.getBook());

        return new CartItemResponseDto(
                cartItem.getId(),
                cartItem.getUserId(),
                cartBook,
                cartItem.getQuantity(),
                cartItem.getPrice(),
                cartBook.getId()
        );
    }

    @Override
    public void copyProperties(CartItemRequestDto requestDto, CartItem entity) {
        if (requestDto.getUserId() != null) {
            entity.setUserId(requestDto.getUserId());
        }
        if (requestDto.getBookId() != null) {
            Book book = bookService.getRepository().findById(requestDto.getBookId()).orElseThrow(
                    () -> new RuntimeException("Book not found")
            );
            entity.setBook(book);
        }
        if (requestDto.getPrice() != null) {
            entity.setPrice(requestDto.getPrice());
        }
        if (requestDto.getQuantity() != null) {
            entity.setQuantity(requestDto.getQuantity());
        }
    }
    @Override
    public List<CartItemResponseDto> getCartItemsByUserId(Long userId) {
        return cartItemRepository.findByUserId(userId).stream()
                .map(this::toResponseDto)
                .toList();
    }

    @Override
    public CartItemResponseDto addCartItem(Long userId, Long bookId, int quantity) {
        List<CartItem> existing = cartItemRepository.findByUserId(userId);
        for (CartItem cartItem : existing) {
            if (cartItem.getBook().getId().equals(bookId)) {
                cartItem.setQuantity(cartItem.getQuantity() + quantity);
                CartItem cartItemSaved = cartItemRepository.save(cartItem);
                return toResponseDto(cartItemSaved);
            }
        }
        Book book = bookService.getRepository().findById(bookId).orElseThrow(
                () -> new RuntimeException("Book not found")
        );
    // create new cart item if not exist
        CartItem cartItem = new CartItem();
        cartItem.setUserId(userId);
        cartItem.setBook(book);
        cartItem.setQuantity(quantity);
        return toResponseDto(cartItemRepository.save(cartItem));
    }

    @Override
    public CartItemResponseDto updateCartItem(Long cartItemId, int quantity) {
        Optional<CartItem> cartItem = cartItemRepository.findById(cartItemId);
        if (cartItem.isPresent()) {
            CartItem existingCartItem = cartItem.get();
            existingCartItem.setQuantity(quantity);
            return toResponseDto(cartItemRepository.save(existingCartItem));
        } else {
            throw new RuntimeException("Cart item not found");
        }
    }

    @Override
    public void deleteCartItem(Long cartItemId) {
        Optional<CartItem> cartItem = cartItemRepository.findById(cartItemId);
        if (cartItem.isPresent()) {
            cartItemRepository.delete(cartItem.get());
        } else {
            throw new RuntimeException("Cart item not found");
        }

    }

    @Override
    public void deleteAllCartItems(Long userId) {
        cartItemRepository.deleteAllByUserId(userId);
    }
}
