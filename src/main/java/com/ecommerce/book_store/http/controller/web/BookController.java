package com.ecommerce.book_store.http.controller.web;

import com.ecommerce.book_store.persistent.entity.Book;
import com.ecommerce.book_store.persistent.entity.Review;
import com.ecommerce.book_store.service.abstraction.BookService;
import com.ecommerce.book_store.service.abstraction.ReviewService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/books")
public class BookController {
    private final BookService bookService;
    private final ReviewService reviewService;

    public BookController(BookService bookService, ReviewService reviewService) {
        this.bookService = bookService;
        this.reviewService = reviewService;
    }

    @GetMapping("/{id}")
    public String getProductDetail(@PathVariable Long id, Model model) {
        if (bookService.getBookById(id).isEmpty()) {
            return "pages/error/404";
        }
        Book book = bookService.getBookById(id).get();
        model.addAttribute("book", book);
        List<Review> reviews = reviewService.getReviewsByBookId(id);
        model.addAttribute("reviews", reviews);
        double avgRating = reviewService.getAverageRatingByBookId(id);
        model.addAttribute("avgRating", avgRating);
        model.addAttribute("CONTENT_TITLE", book.getTitle());
        model.addAttribute("LAYOUT_TITLE", "BookStore");
        return "pages/detail/detailBook";
    }

    @GetMapping("/search")
    public String searchBooks(@RequestParam("term") String term, Model model, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        model.addAttribute("page", bookService.findBooksContainingTitle(term, page, size));
        model.addAttribute("url", "/books/search?term=" + term);
        model.addAttribute("CONTENT_TITLE", "Search: " + term);
        model.addAttribute("LAYOUT_TITLE", "BookStore");
        return "pages/search/index";
    }
}