package com.ecommerce.book_store.http.controller.web;

import com.ecommerce.book_store.persistent.entity.Review;
import com.ecommerce.book_store.service.abstraction.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
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
    public List<Review> getReviews(@PathVariable Long bookId) {
        return reviewService.getReviewsByBookId(bookId);
    }

    @PostMapping("/add")
    @ResponseBody
    public Map<String, Object> addReview(@RequestBody Map<String, Object> payload) {
        Long bookId = Long.parseLong(payload.get("bookId").toString());
        Long userId = Long.parseLong(payload.get("userId").toString());
        int rating = Integer.parseInt(payload.get("rating").toString());
        String comment = payload.get("comment").toString();

        Review review = reviewService.addReview(bookId, userId , rating, comment);
        Map<String, Object> response = new HashMap<>();
        response.put("success", review != null);
        return response;
    }
}
