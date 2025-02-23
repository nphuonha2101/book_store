package com.ecommerce.book_store.service.implement;

import com.ecommerce.book_store.http.dto.request.implement.BookRequestDto;
import com.ecommerce.book_store.http.dto.response.implement.BookResponseDto;
import com.ecommerce.book_store.http.dto.response.implement.CategoryResponseDto;
import com.ecommerce.book_store.persistent.entity.AbstractEntity;
import com.ecommerce.book_store.persistent.entity.Book;
import com.ecommerce.book_store.persistent.entity.BookImage;
import com.ecommerce.book_store.persistent.entity.Category;
import com.ecommerce.book_store.persistent.repository.abstraction.BookRepository;
import com.ecommerce.book_store.service.abstraction.BookService;
import com.ecommerce.book_store.service.abstraction.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.context.config.ConfigDataResourceNotFoundException;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
public class BookServiceImpl
        extends IServiceImpl<BookRequestDto, BookResponseDto, Book>
        implements BookService {

    private final CategoryService categoryService;
    private final FirebaseStorageService firebaseStorageService;
    private final BookRepository bookRepository;

    @Autowired
    public BookServiceImpl(BookRepository repository, CategoryService categoryService, FirebaseStorageService firebaseStorageService, BookRepository bookRepository) {
        super(repository);
        this.categoryService = categoryService;
        this.firebaseStorageService = firebaseStorageService;
        this.bookRepository = bookRepository;
    }

    @Override
    public Book toEntity(BookRequestDto requestDto) {
        // Upload cover image if exists
        String coverImageUrl = null;
        if (requestDto.getCoverImage() != null && !requestDto.getCoverImage().isEmpty()) {
            coverImageUrl = firebaseStorageService.uploadFile(
                    requestDto.getCoverImage(),
                    "books/covers"
            );
        }

        // Upload multiple book images if exists
        List<String> imageUrls = new ArrayList<>();
        if (requestDto.getBookImages() != null && !requestDto.getBookImages().isEmpty()) {
            for (MultipartFile image : requestDto.getBookImages()) {
                String imageUrl = firebaseStorageService.uploadFile(
                        image,
                        "books/images"
                );
                imageUrls.add(imageUrl);
            }
        }

        // Create book entity
        Book result = new Book(
                requestDto.getTitle(),
                requestDto.getAuthorName(),
                requestDto.getDescription(),
                requestDto.getIsbn(),
                coverImageUrl,
                requestDto.getPrice().doubleValue(),
                requestDto.getQuantity(),
                requestDto.isAvailable(),
                requestDto.getPublishedAt(),
                null
        );

        // Set category
        Category category = categoryService.findById(requestDto.getCategoryId());
        result.setCategory(category);

        // Create BookImage entities from uploaded URLs
        List<BookImage> bookImages = new ArrayList<>();
        for (String imageUrl : imageUrls) {
            BookImage bookImage = new BookImage(imageUrl, result);
            bookImages.add(bookImage);
        }
        result.setImages(bookImages);

        return result;
    }

    @Override
    public BookResponseDto toResponseDto(AbstractEntity entity) {
        Book book = (Book) entity;

        CategoryResponseDto categoryResponseDto = new CategoryResponseDto(
                book.getCategory().getId(),
                book.getCategory().getName(),
                book.getCategory().getDescription()
        );

        return new BookResponseDto(
                book.getId(),
                book.getTitle(),
                book.getAuthorName(),
                book.getDescription(),
                book.getIsbn(),
                book.getImages(),
                book.getPrice(),
                book.getQuantity(),
                book.isAvailable(),
                categoryResponseDto,
                book.getPublishedAt()
        );
    }


    @Override
    public void copyProperties(BookRequestDto requestDto, Book entity) {
        Category category = categoryService.findById(requestDto.getCategoryId());

        // Check if a new cover image is provided
        if (requestDto.getCoverImage() != null && !requestDto.getCoverImage().isEmpty()) {
            String coverImageUrl = firebaseStorageService.uploadFile(
                    requestDto.getCoverImage(),
                    "books/covers"
            );
            entity.setCoverImage(coverImageUrl);
        }

        // Upload multiple book images if exists
        List<String> imageUrls = new ArrayList<>();
        if (requestDto.getBookImages() != null && !requestDto.getBookImages().isEmpty()) {
            for (MultipartFile image : requestDto.getBookImages()) {
                String imageUrl = firebaseStorageService.uploadFile(
                        image,
                        "books/images"
                );
                imageUrls.add(imageUrl);
            }
        }

        List<BookImage> bookImages = new ArrayList<>();
        for (String imageUrl : imageUrls) {
            BookImage bookImage = new BookImage(imageUrl, entity);
            bookImages.add(bookImage);
        }

        entity.setTitle(requestDto.getTitle());
        entity.setAuthorName(requestDto.getAuthorName());
        entity.setDescription(requestDto.getDescription());
        entity.setIsbn(requestDto.getIsbn());
        entity.setPrice(requestDto.getPrice().doubleValue());
        entity.setQuantity(requestDto.getQuantity());
        entity.setAvailable(requestDto.isAvailable());
        entity.setPublishedAt(requestDto.getPublishedAt());
        entity.setCategory(category);
        entity.setImages(bookImages);
    }

//    @Cacheable(value = "books", key = "#id")
//    @Override
//    public Book findById(Long id) {
//        return super.findById(id);
//    }
//
//    @Cacheable(value = "books")
//    @Override
//    public List<Book> findAll() {
//        return super.findAll();
//    }
//
//    @CachePut(value = "books", key = "#result.id")
//    @Override
//    public Book save(BookRequestDto requestDto) {
//        return super.save(requestDto);
//    }
//
//    @CachePut(value = "books", key = "#id")
//    @Override
//    public Book update(BookRequestDto requestDto, Long id) {
//        return super.update(requestDto, id);
//    }
//
//    @CacheEvict(value = "books", key = "#id")
//    @Override
//    public boolean deleteById(Long id) {
//        return super.deleteById(id);
//    }

    @Override
    public Optional<Book> getBookById(Long id) {
            return bookRepository.findById(id);
    }
}
