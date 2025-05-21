package com.ecommerce.book_store.service.implement;

import com.ecommerce.book_store.http.dto.request.implement.BookImageRequestDto;
import com.ecommerce.book_store.http.dto.response.implement.BookImageResponseDto;
import com.ecommerce.book_store.persistent.entity.AbstractEntity;
import com.ecommerce.book_store.persistent.entity.Book;
import com.ecommerce.book_store.persistent.entity.BookImage;
import com.ecommerce.book_store.persistent.repository.abstraction.BookImageRepository;
import com.ecommerce.book_store.service.abstraction.BookImageService;
import com.ecommerce.book_store.service.abstraction.BookService;
import lombok.Getter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookImageServiceImpl extends IServiceImpl<BookImageRequestDto, BookImageResponseDto, BookImage> implements BookImageService {
    private final BookService bookService;
    private final FirebaseStorageService firebaseStorageService;

    public BookImageServiceImpl(BookImageRepository repository, BookService bookService, FirebaseStorageService firebaseStorageService) {
        super(repository);
        this.bookService = bookService;
        this.firebaseStorageService = firebaseStorageService;
    }

    @Override
    public BookImage toEntity(BookImageRequestDto requestDto) {
        Book book = bookService.getRepository().findById(requestDto.getBookId()).orElseThrow(
                () -> new RuntimeException("Book not found")
        );
        BookImage bookImage = new BookImage();
        bookImage.setBook(book);

        // Upload book image
        String imageUrl = firebaseStorageService.uploadFile(
                requestDto.getImage(),
                "books/images"
        );
        bookImage.setUrl(imageUrl);
        bookImage.setBookId(requestDto.getBookId());
        return bookImage;

    }

    @Override
    public BookImageResponseDto toResponseDto(AbstractEntity entity) {
        if (entity == null) {
            return null;
        }
        BookImage bookImage = (BookImage) entity;
        return new BookImageResponseDto(bookImage.getId(), bookImage.getBook().getId(), bookImage.getUrl());
    }

    @Override
    public void copyProperties(BookImageRequestDto requestDto, BookImage entity) {
        Book book = bookService.getRepository().findById(requestDto.getBookId()).orElseThrow(
                () -> new RuntimeException("Book not found")
        );
        entity.setBook(book);

        // Upload book image
        String imageUrl = firebaseStorageService.uploadFile(
                requestDto.getImage(),
                "books/images"
        );
        entity.setUrl(imageUrl);
    }

    @Override
    public Page<BookImageResponseDto> findAllByBookId(Long bookId, Pageable pageable) {
        Page<BookImage> bookImages = ((BookImageRepository) repository).findAllByBookId(bookId, pageable);
        return bookImages.map(this::toResponseDto);
    }
}
