package com.ecommerce.book_store.http.controller.api.book;

import com.ecommerce.book_store.service.abstraction.BookService;
import org.springframework.data.domain.Pageable;
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
    public ResponseEntity<?> searchBooks(@RequestParam("term") String term,
                                         @RequestParam(defaultValue = "0") int page,
                                         @RequestParam(defaultValue = "10") int size
    ) {
        try {
            return ResponseEntity.ok(bookService.findBooksContainingTitle(term, page, size));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/suggest")
    public ResponseEntity<?> suggestBooks(@RequestBody List<String> terms,
                                          @RequestParam(defaultValue = "0") int page,
                                          @RequestParam(defaultValue = "10") int size
    ) {
        try {
            return ResponseEntity.ok(bookService.findBooksByTitleIn(terms, page, size));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/filter")
    public ResponseEntity<?> filterBooks(@RequestParam(required = false) String authorName,
                                         @RequestParam(required = false) String title,
                                         @RequestParam(required = false) List<Long> categoryIds,
                                         @RequestParam(required = false) Double minPrice,
                                         @RequestParam(required = false) Double maxPrice,
                                         Pageable pageable
    ) {
        try {
            return ResponseEntity.ok(bookService.filter(authorName, title, categoryIds, minPrice, maxPrice, pageable.getPageNumber(), pageable.getPageSize()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
