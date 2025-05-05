package com.ecommerce.book_store.service.abstraction;

import com.ecommerce.book_store.http.dto.request.implement.ReviewRequestDto;
import com.ecommerce.book_store.http.dto.response.implement.ReviewResponseDto;
import com.ecommerce.book_store.persistent.entity.Review;

import java.util.List;
public interface ReviewService extends IService<ReviewRequestDto, ReviewResponseDto, Review> {
    List<Review> getReviewsByBookId(Long bookId);
    Review addReview(Long bookId, Long userId, int rating, String comment);
    Review editReview(Long reviewId, Long userId, int rating, String comment);
    Review deleteReview(Long reviewId);
    double getAverageRatingByBookId(Long bookId);
}

