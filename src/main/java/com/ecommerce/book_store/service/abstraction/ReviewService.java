package com.ecommerce.book_store.service.abstraction;

import com.ecommerce.book_store.http.dto.request.implement.ReviewRequestDto;
import com.ecommerce.book_store.http.dto.response.implement.ReviewResponseDto;
import com.ecommerce.book_store.persistent.entity.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
public interface ReviewService extends IService<ReviewRequestDto, ReviewResponseDto, Review> {
    Page<ReviewResponseDto> getReviewsByBookId(Long bookId, Pageable pageable);
    boolean isUserReviewedBook(Long userId, Long bookId);
    ReviewResponseDto addReview(Long bookId, Long userId, int rating, String comment);
    ReviewResponseDto editReview(Long reviewId, Long userId, int rating, String comment);
    double getAverageRatingByBookId(Long bookId);
    Long countByBookId(Long bookId);
}

