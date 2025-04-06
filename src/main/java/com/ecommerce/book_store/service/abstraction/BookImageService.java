package com.ecommerce.book_store.service.abstraction;

import com.ecommerce.book_store.http.dto.request.implement.BookImageRequestDto;
import com.ecommerce.book_store.http.dto.response.implement.BookImageResponseDto;
import com.ecommerce.book_store.persistent.entity.BookImage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BookImageService extends IService<BookImageRequestDto, BookImageResponseDto, BookImage> {
    Page<BookImageResponseDto> findAllByBookId(Long bookId, Pageable pageable);
}
