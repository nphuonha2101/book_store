package com.ecommerce.book_store.service.implement;

import com.ecommerce.book_store.http.dto.request.implement.OrderItemRequestDto;
import com.ecommerce.book_store.http.dto.response.implement.OrderItemResponseDto;
import com.ecommerce.book_store.persistent.entity.AbstractEntity;
import com.ecommerce.book_store.persistent.entity.Book;
import com.ecommerce.book_store.persistent.entity.OrderItem;
import com.ecommerce.book_store.persistent.repository.abstraction.OrderItemRepository;
import com.ecommerce.book_store.service.abstraction.BookService;
import com.ecommerce.book_store.service.abstraction.OrderItemService;
import com.ecommerce.book_store.service.abstraction.OrderService;
import org.springframework.stereotype.Service;

@Service
public class OrderItemServiceImpl extends IServiceImpl<OrderItemRequestDto, OrderItemResponseDto, OrderItem>
    implements OrderItemService
{
    private final BookService bookService;

    public OrderItemServiceImpl(OrderItemRepository repository, OrderService orderService, BookService bookService) {
        super(repository);
        this.bookService = bookService;
    }

    @Override
    public OrderItem toEntity(OrderItemRequestDto requestDto) {
        Book book = bookService.getRepository().findById(requestDto.getBookId()).orElseThrow(
            () -> new RuntimeException("Book not found")
        );
        return new OrderItem(
                null,
                book,
                requestDto.getQuantity(),
                requestDto.getPrice()
        );
    }

    @Override
    public OrderItemResponseDto toResponseDto(AbstractEntity entity) {
        if (entity == null)
            return null;

        OrderItem orderItem = (OrderItem) entity;

        return new OrderItemResponseDto(
                orderItem.getId(),
                orderItem.getOrder().getId(),
                bookService.toResponseDto(orderItem.getBook()),
                orderItem.getQuantity(),
                orderItem.getPrice()
        );
    }

    @Override
    public void copyProperties(OrderItemRequestDto requestDto, OrderItem entity) {
        if (requestDto.getBookId() != null) {
            Book book = bookService.getRepository().findById(requestDto.getBookId()).orElseThrow(
                () -> new RuntimeException("Book not found")
            );
            entity.setBook(book);
        }

        if (requestDto.getQuantity() != 0) {
            entity.setQuantity(requestDto.getQuantity());
        }

        if (requestDto.getPrice() != 0) {
            entity.setPrice(requestDto.getPrice());
        }
    }
}
