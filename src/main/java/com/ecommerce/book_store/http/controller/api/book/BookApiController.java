package com.ecommerce.book_store.http.controller.api.book;

import com.ecommerce.book_store.http.ApiResponse;
import com.ecommerce.book_store.http.dto.response.implement.BookResponseDto;
import com.ecommerce.book_store.service.abstraction.BookService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RequestMapping("/api/v1/books")
@RestController
public class BookApiController {
    private final BookService bookService;

    public BookApiController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping("/all")
    public ResponseEntity<ApiResponse<List<BookResponseDto>>> getAllBooks(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        try {
            Page<BookResponseDto> books = bookService.findAll(PageRequest.of(page, size));
            return books != null ? ApiResponse.successWithPagination(books, "Tất cả sách được lấy thành công") : ApiResponse.error("Không tìm thấy", HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<BookResponseDto>> getBookById(@PathVariable("id") Long id) {
        try {
            BookResponseDto book = bookService.findById(id);
            return book != null ? ApiResponse.success(book, "Sách được lấy thành công") : ApiResponse.error("Không tìm thấy", HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<BookResponseDto>>> searchBooks(@RequestParam("term") String term,
                                                                          @RequestParam(defaultValue = "0") int page,
                                                                          @RequestParam(defaultValue = "10") int size
    ) {
        try {
            Page<BookResponseDto> books = bookService.findBooksContainingTitle(term, page, size);
            return books != null ? ApiResponse.successWithPagination(books, "Tất cả sách được lấy thành công") : ApiResponse.error("Không tìm thấy", HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/suggest")
    public ResponseEntity<ApiResponse<List<BookResponseDto>>> suggestBooks(@RequestBody List<String> terms,
                                             @RequestParam(defaultValue = "0") int page,
                                             @RequestParam(defaultValue = "4") int size
    ) {
        try {
            Page<BookResponseDto> books = bookService.findBooksByTitleIn(terms, page, size);
            return books != null ? ApiResponse.successWithPagination(books, "Tất cả sách được lấy thành công") : ApiResponse.error("Không tìm thấy", HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/filter")
    public ResponseEntity<ApiResponse<List<BookResponseDto>>> filterBooks(
            @RequestParam(value = "authorName", required = false) String authorName,
            @RequestParam(value = "title", required = false) String title,
            @RequestParam(value = "categoryIds", required = false) List<Long> categoryIds,
            @RequestParam(value = "minPrice", required = false) Double minPrice,
            @RequestParam(value = "maxPrice", required = false) Double maxPrice,
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "size", defaultValue = "10") Integer size
    ) {
        try {
            if (categoryIds != null && categoryIds.isEmpty()) {
                categoryIds = null;
            }

            Page<BookResponseDto> books = bookService.filter(
                    authorName,
                    title,
                    categoryIds,
                    minPrice,
                    maxPrice,
                    page,
                    size
            );

            return books != null && !books.isEmpty()
                    ? ApiResponse.successWithPagination(books, "Tất cả sách được lấy thành công")
                    : ApiResponse.success(new ArrayList<>(), "Không tìm thấy sách nào");

        } catch (Exception e) {
            return ApiResponse.error(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
