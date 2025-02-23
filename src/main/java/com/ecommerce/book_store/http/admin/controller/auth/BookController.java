package com.ecommerce.book_store.http.admin.controller.auth;

import com.ecommerce.book_store.persistent.entity.Review;
import com.ecommerce.book_store.service.abstraction.BookService;
import com.ecommerce.book_store.service.abstraction.ReviewService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

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
        return bookService.getBookById(id)
                .map(book -> {
                    model.addAttribute("book", book);
                    List<Review> reviews = reviewService.getReviewsByBookId(id);
                    model.addAttribute("reviews", reviews);
                    double avgRating = reviewService.getAverageRatingByBookId(id);
                    model.addAttribute("avgRating", avgRating);
                    return "pages/auth/detailBook";
                })
                .orElse("error/404");
    }
}