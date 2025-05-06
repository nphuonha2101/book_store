package com.ecommerce.book_store.http.controller.web;

import com.ecommerce.book_store.core.security.JwtUtils;
import com.ecommerce.book_store.http.ApiResponse;
import com.ecommerce.book_store.http.dto.request.implement.ReviewRequestDto;
import com.ecommerce.book_store.http.dto.response.implement.ReviewResponseDto;
import com.ecommerce.book_store.service.abstraction.OrderItemService;
import com.ecommerce.book_store.service.abstraction.ReviewService;
import com.ecommerce.book_store.service.abstraction.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/reviews")
public class ReviewApiController {
    private final ReviewService reviewService;
    private final OrderItemService orderItemService;
    private final JwtUtils jwtUtils;
    private final UserService userService;

    public ReviewApiController(
            ReviewService reviewService, OrderItemService orderItemService,
            JwtUtils jwtUtils, UserService userService) {
        this.reviewService = reviewService;
        this.orderItemService = orderItemService;
        this.jwtUtils = jwtUtils;
        this.userService = userService;
    }

    @GetMapping("/{bookId}")
    public ResponseEntity<ApiResponse<List<ReviewResponseDto>>> getReviews(@PathVariable Long bookId,
                                                          @RequestParam(defaultValue = "0") int page,
                                                          @RequestParam(defaultValue = "10") int size) {
        Page<ReviewResponseDto> reviews = reviewService.getReviewsByBookId(bookId, PageRequest.of(page, size));
        if (reviews.isEmpty()) {
            return ApiResponse.error("No review found", HttpStatus.NOT_FOUND);
        }
        return ApiResponse.successWithPagination(reviews, "Get all reviews successfully");
    }

    @PostMapping("")
    public ResponseEntity<ApiResponse<ReviewResponseDto>> addReview(@RequestBody ReviewRequestDto request) {
        try {
            ReviewResponseDto added = reviewService.addReview(request.getBookId(), request.getUserId(), request.getRating(), request.getComment());
            return ApiResponse.success(added, "Review added successfully");
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/metadata/{bookId}")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getReviewsMetadataOfBook(@PathVariable Long bookId) {
        try {
            double averageRating = reviewService.getAverageRatingByBookId(bookId);
            Long reviewCount = reviewService.countByBookId(bookId);
            Map<String, Object> metadata = Map.of(
                    "averageRating", averageRating,
                    "reviewCount", reviewCount
            );
            return ApiResponse.success(metadata, "Get reviews metadata successfully");
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @GetMapping("/can-review/{bookId}")
    public ResponseEntity<ApiResponse<Boolean>> canUserReviewBook(@PathVariable Long bookId, @RequestHeader("Authorization") String token) {
        try {
            String jwtToken = token.substring(7);
            String email = jwtUtils.extractUserEmail(jwtToken);
            Long userId = userService.findIdByEmail(email).orElseThrow(
                    () -> new RuntimeException("User not found")
            );

            boolean isUserReviewed = reviewService.isUserReviewedBook(userId, bookId);
            boolean isUserPurchased = orderItemService.isUserPurchasedBook(userId, bookId);
            // User đã mua sách và chưa review thì có thể review
            boolean canReview = !isUserReviewed && isUserPurchased;

            return ApiResponse.success(canReview, "Check review permission successfully");
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
