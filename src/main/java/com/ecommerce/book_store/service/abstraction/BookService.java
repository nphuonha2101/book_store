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
    Page<BookResponseDto> findBooksContainingTitle(String title, int page, int size);
    Page<BookResponseDto> findBooksByTitleIn(List<String> titles, int page, int size);
    Page<BookResponseDto> filter(String authorName, String title, List<Long> categoryIds, Double minPrice, Double maxPrice,  int page, int size);
    Page<BookResponseDto> searchRelevanceByKeyword(String keyword, Pageable pageable);
}
