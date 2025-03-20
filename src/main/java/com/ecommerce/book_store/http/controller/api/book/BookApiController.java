package com.ecommerce.book_store.http.controller.api.book;

import com.ecommerce.book_store.http.ApiResponse;
import com.ecommerce.book_store.persistent.entity.Book;
import com.ecommerce.book_store.service.abstraction.BookService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api/v1/books")
@RestController
public class BookApiController {
    private final BookService bookService;

    public BookApiController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<Book>>> searchBooks(@RequestParam("term") String term,
                                                   @RequestParam(defaultValue = "0") int page,
                                                   @RequestParam(defaultValue = "10") int size
    ) {
        try {
            Page<Book> books = bookService.findBooksContainingTitle(term, page, size);
            return books != null ? ApiResponse.successWithPagination(books, "Tất cả sách được lấy thành công") : ApiResponse.error("Không tìm thấy", HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/suggest")
    public ResponseEntity<ApiResponse<List<Book>>> suggestBooks(@RequestBody List<String> terms,
                                             @RequestParam(defaultValue = "0") int page,
                                             @RequestParam(defaultValue = "10") int size
    ) {
        try {
            Page<Book> books = bookService.findBooksByTitleIn(terms, page, size);
            return books != null ? ApiResponse.successWithPagination(books, "Tất cả sách được lấy thành công") : ApiResponse.error("Không tìm thấy", HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/filter")
    public ResponseEntity<ApiResponse<List<Book>>> filterBooks(@RequestParam(required = false) String authorName,
                                         @RequestParam(required = false) String title,
                                         @RequestParam(required = false) List<Long> categoryIds,
                                         @RequestParam(required = false) Double minPrice,
                                         @RequestParam(required = false) Double maxPrice,
                                         Pageable pageable
    ) {
        try {
            Page<Book> books = this.bookService.filter(authorName, title, categoryIds, minPrice, maxPrice, pageable.getPageNumber(), pageable.getPageSize());
            return books != null ? ApiResponse.successWithPagination(books, "Tất cả sách được lấy thành công") : ApiResponse.error("Không tìm thấy", HttpStatus.NOT_FOUND);

        } catch (Exception e) {
            return ApiResponse.error(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
