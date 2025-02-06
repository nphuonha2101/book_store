package com.ecommerce.book_store.service.abstraction;

import com.ecommerce.book_store.http.dto.request.implement.BookRequestDto;
import com.ecommerce.book_store.http.dto.response.implement.BookResponseDto;
import com.ecommerce.book_store.persistent.entity.Book;

import java.util.Optional;

public interface BookService
        extends IService<BookRequestDto, BookResponseDto, Book> {
    Optional<Book> getBookById(Long id);
}
