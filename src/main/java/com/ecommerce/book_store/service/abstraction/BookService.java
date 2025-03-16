package com.ecommerce.book_store.service.abstraction;

import com.ecommerce.book_store.http.dto.request.implement.BookRequestDto;
import com.ecommerce.book_store.http.dto.response.implement.BookResponseDto;
import com.ecommerce.book_store.persistent.entity.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface BookService
        extends IService<BookRequestDto, BookResponseDto, Book> {
    Optional<Book> getBookById(Long id);
    Page<Book> findBooksContainingTitle(String title, int page, int size);
    Page<Book> findBooksByTitleIn(List<String> titles, int page, int size);
    Page<Book> filter(String authorName, String title, List<Long> categoryIds, Double minPrice, Double maxPrice,  int page, int size);
}
