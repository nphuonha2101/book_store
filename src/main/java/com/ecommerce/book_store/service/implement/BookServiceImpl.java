package com.ecommerce.book_store.service.implement;

import com.ecommerce.book_store.http.dto.request.implement.BookRequestDto;
import com.ecommerce.book_store.http.dto.response.implement.BookResponseDto;
import com.ecommerce.book_store.http.dto.response.implement.CategoryResponseDto;
import com.ecommerce.book_store.persistent.entity.AbstractEntity;
import com.ecommerce.book_store.persistent.entity.Book;
import com.ecommerce.book_store.persistent.entity.BookImage;
import com.ecommerce.book_store.persistent.entity.Category;
import com.ecommerce.book_store.persistent.repository.abstraction.BookRepository;
import com.ecommerce.book_store.service.abstraction.BookImageService;
import com.ecommerce.book_store.service.abstraction.BookService;
import com.ecommerce.book_store.service.abstraction.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Service
public class BookServiceImpl
        extends IServiceImpl<BookRequestDto, BookResponseDto, Book>
        implements BookService {

    private final CategoryService categoryService;
    private final FirebaseStorageService firebaseStorageService;
    private final BookImageService bookImageService;

    @Autowired
    public BookServiceImpl(BookRepository repository, CategoryService categoryService,
            FirebaseStorageService firebaseStorageService, @Lazy BookImageService bookImageService) {
        super(repository);
        this.categoryService = categoryService;
        this.firebaseStorageService = firebaseStorageService;
        this.bookImageService = bookImageService;
    }

    @Override
    public Book toEntity(BookRequestDto requestDto) {
        // Upload cover image if exists
        String coverImageUrl = null;
        if (requestDto.getCoverImage() != null && !requestDto.getCoverImage().isEmpty()) {
            coverImageUrl = firebaseStorageService.uploadFile(
                    requestDto.getCoverImage(),
                    "books/covers");
        }

        // Upload multiple book images if exists
        List<String> imageUrls = new ArrayList<>();
        if (requestDto.getBookImages() != null && !requestDto.getBookImages().isEmpty()) {
            for (MultipartFile image : requestDto.getBookImages()) {
                String imageUrl = firebaseStorageService.uploadFile(
                        image,
                        "books/images");
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
                requestDto.getPrice(),
                requestDto.getQuantity(),
                requestDto.getIsAvailable(),
                requestDto.getPublishedAt(),
                null);

        // Set category
        Category category = categoryService.getRepository().findById(requestDto.getCategoryId()).orElseThrow(
                () -> new RuntimeException("Category not found"));
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
        if (entity == null) {
            return null;
        }

        Book book = (Book) entity;
        CategoryResponseDto categoryResponseDto = categoryService.toResponseDto(book.getCategory());

        return new BookResponseDto(
                book.getId(),
                book.getTitle(),
                book.getAuthorName(),
                book.getDescription(),
                book.getIsbn(),
                book.getImages().stream()
                        .map(bookImageService::toResponseDto)
                        .toList(),
                book.getCoverImage(),
                book.getPrice(),
                book.getQuantity(),
                book.isAvailable(),
                book.getPublishedAt(),
                categoryResponseDto);
    }

    @Override
    public void copyProperties(BookRequestDto requestDto, Book entity) {
        Category category = categoryService.getRepository().findById(requestDto.getCategoryId()).orElseThrow(
                () -> new RuntimeException("Category not found"));

        // Check if a new cover image is provided
        if (requestDto.getCoverImage() != null && !requestDto.getCoverImage().isEmpty()) {
            String coverImageUrl = firebaseStorageService.uploadFile(
                    requestDto.getCoverImage(),
                    "books/covers");
            entity.setCoverImage(coverImageUrl);
        }

        // Upload multiple book images if exists
        List<String> imageUrls = new ArrayList<>();
        if (requestDto.getBookImages() != null && !requestDto.getBookImages().isEmpty()) {
            for (MultipartFile image : requestDto.getBookImages()) {
                String imageUrl = firebaseStorageService.uploadFile(
                        image,
                        "books/images");
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
        entity.setPrice(requestDto.getPrice());
        entity.setQuantity(requestDto.getQuantity());
        entity.setAvailable(requestDto.getIsAvailable());
        entity.setPublishedAt(requestDto.getPublishedAt());
        entity.setCategory(category);
        entity.setImages(bookImages);
    }

    @Override
    @Cacheable(value = "books", key = "#id")
    public BookResponseDto findById(Long id) {
        return super.findById(id);
    }

    @Override
    @Cacheable(value = "allBooks")
    public List<BookResponseDto> findAll() {
        return super.findAll();
    }

    @Override
    @CachePut(value = "books", key = "#result.id")
    @CacheEvict(value = "allBooks", allEntries = true)
    public BookResponseDto save(BookRequestDto requestDto) {
        return super.save(requestDto);
    }

    @Override
    @Caching(put = { @CachePut(value = "books", key = "#id") }, evict = {
            @CacheEvict(value = "allBooks", allEntries = true) })
    public BookResponseDto update(BookRequestDto requestDto, Long id) {
        return super.update(requestDto, id);
    }

    @Override
    @Caching(evict = {
            @CacheEvict(value = "books", key = "#id"),
            @CacheEvict(value = "allBooks", allEntries = true)
    })
    public boolean deleteById(Long id) {
        return super.deleteById(id);
    }

    @Override
    @Cacheable(value = "booksByTitle", key = "#title + '-' + #page + '-' + #size")
    public Page<BookResponseDto> findBooksContainingTitle(String title, int page, int size) {
        Pageable pageable = Pageable.ofSize(size).withPage(page);
        return ((BookRepository) getRepository()).findByTitleContaining(title, pageable).map(this::toResponseDto);
    }

    @Override
    @Cacheable(value = "booksByTitles", key = "#titles.hashCode() + '-' + #page + '-' + #size")
    public Page<BookResponseDto> findBooksByTitleIn(List<String> titles, int page, int size) {
        Pageable pageable = Pageable.ofSize(size).withPage(page);
        return ((BookRepository) getRepository()).findByTitleIn(titles, pageable)
                .map(this::toResponseDto);
    }

    @Override
    @Cacheable(value = "booksByFilter", key = "#authorName + '-' + #title + '-' + #categoryIds + '-' + #minPrice + '-' + #maxPrice + '-' + #page + '-' + #size")
    public Page<BookResponseDto> filter(String authorName, String title, List<Long> categoryIds, Double minPrice,
            Double maxPrice, int page, int size) {
        Pageable pageable = Pageable.ofSize(size).withPage(page);
        return ((BookRepository) getRepository()).filter(authorName, title, categoryIds, minPrice, maxPrice, pageable)
                .map(this::toResponseDto);
    }

    @Override
    @Cacheable(value = "booksByKeyword", key = "#keyword + '-' + #pageable.pageNumber + '-' + #pageable.pageSize")
    public Page<BookResponseDto> searchRelevanceByKeyword(String keyword, Pageable pageable) {
        return ((BookRepository) getRepository()).searchByKeyword(keyword, pageable)
                .map(this::toResponseDto);
    }

    @Override
    @Cacheable(value = "sortedBooks", key = "#sort.toString()")
    public List<BookResponseDto> findAll(Sort sort) {
        return super.findAll(sort);
    }

    @Override
    @Cacheable(value = "pagedBooks", key = "#pageable.pageNumber + '-' + #pageable.pageSize + '-' + #pageable.sort")
    public Page<BookResponseDto> findAll(Pageable pageable) {
        return super.findAll(pageable);
    }

    @Override
    @Cacheable(value = "booksList", key = "#entities.hashCode()")
    public List<BookResponseDto> toResponseDto(List<AbstractEntity> entities) {
        return super.toResponseDto(entities);
    }
}
