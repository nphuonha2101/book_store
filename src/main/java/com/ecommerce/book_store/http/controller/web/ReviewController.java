package com.ecommerce.book_store.http.controller.web;

import com.ecommerce.book_store.http.ApiResponse;
import com.ecommerce.book_store.persistent.entity.Review;
import com.ecommerce.book_store.service.abstraction.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/reviews")
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

    @GetMapping("/{bookId}")
    public ResponseEntity<ApiResponse<Object>> getReviews(@PathVariable Long bookId) {
        List<Review> reviews = reviewService.getReviewsByBookId(bookId);
        if (reviews.isEmpty()) {
            return ApiResponse.error("No review found", HttpStatus.NOT_FOUND);
        }
        return ApiResponse.success(reviews, "Get all reviews successfully");
    }

    @PostMapping("/add")
    @ResponseBody
    public ResponseEntity<ApiResponse<Object>> addReview(@RequestBody Map<String, Object> payload) {
        try {
            Long bookId = Long.parseLong(payload.get("bookId").toString());
            Long userId = Long.parseLong(payload.get("userId").toString());
            int rating = Integer.parseInt(payload.get("rating").toString());
            String comment = payload.get("comment").toString();

            Review review = reviewService.addReview(bookId, userId, rating, comment);

            return review != null ? ApiResponse.success(review, "Add review successfully") : ApiResponse.error("Add review failed", HttpStatus.BAD_REQUEST);

        } catch (Exception e) {
            return ApiResponse.error(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
