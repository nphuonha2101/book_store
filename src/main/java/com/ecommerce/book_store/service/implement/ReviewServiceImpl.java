package com.ecommerce.book_store.service.implement;

import com.ecommerce.book_store.http.dto.request.implement.ReviewRequestDto;
import com.ecommerce.book_store.http.dto.response.implement.BookResponseDto;
import com.ecommerce.book_store.http.dto.response.implement.ReviewResponseDto;
import com.ecommerce.book_store.http.dto.response.implement.UserResponseDto;
import com.ecommerce.book_store.persistent.entity.*;
import com.ecommerce.book_store.persistent.repository.abstraction.ReviewRepository;
import com.ecommerce.book_store.service.abstraction.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ReviewServiceImpl extends IServiceImpl<ReviewRequestDto, ReviewResponseDto, Review>
        implements ReviewService {
    private final ReviewRepository reviewRepository;
    private final BookService bookService;
    private final UserService userService;

    public ReviewServiceImpl(ReviewRepository repository, BookService bookService, UserService userService) {
        super(repository);
        this.reviewRepository = repository;
        this.bookService = bookService;
        this.userService = userService;
    }
    @Override
    public Review toEntity(ReviewRequestDto requestDto) {
        Review result = new Review(
                null,
                null,
                requestDto.getRating(),
                requestDto.getComment()
        );
        User user = userService.getRepository().findById(requestDto.getUserId()).orElseThrow(
                () -> new RuntimeException("User not found")
        );
        Book book = bookService.getRepository().findById(requestDto.getBookId()).orElseThrow(
                () -> new RuntimeException("Book not found")
        );
        result.setUser(user);
        result.setBook(book);
        return result;
    }
    @Override
    public ReviewResponseDto toResponseDto(AbstractEntity entity) {
        if (entity == null) {
            return null;
        }

        Review review = (Review) entity;
        UserResponseDto userResponseDto = userService.toResponseDto(review.getUser());
        BookResponseDto bookResponseDto = bookService.toResponseDto(review.getBook());

        return new ReviewResponseDto(
                review.getId(),
                userResponseDto,
                bookResponseDto,
                review.getRating(),
                review.getComment(),
                review.getCreatedAt()
        );
    }

    @Override
    public void copyProperties(ReviewRequestDto requestDto, Review entity) {
        entity.setRating(requestDto.getRating());
        entity.setComment(requestDto.getComment());
    }

    @Override
    public Page<ReviewResponseDto> getReviewsByBookId(Long bookId, Pageable pageable) {
        return reviewRepository.findByBookId(bookId, pageable).map(this::toResponseDto);
    }

    @Override
    public boolean isUserReviewedBook(Long userId, Long bookId) {
        return reviewRepository.isUserReviewedBook(userId, bookId);
    }

    @Override
    public ReviewResponseDto addReview(Long bookId, Long userId, int rating, String comment) {
        boolean isUserReviewed = reviewRepository.isUserReviewedBook(userId, bookId);
        if (isUserReviewed) {
            throw new RuntimeException("User has already reviewed this book");
        }
        Review review = new Review();
        review.setBook(bookService.getRepository().findById(bookId).orElseThrow(
                () -> new RuntimeException("Book not found")
        ));
        review.setUser(userService.getRepository().findById(userId).orElseThrow(
                () -> new RuntimeException("User not found")
        ));
        review.setRating(rating);
        review.setComment(comment);
        reviewRepository.save(review);
        return toResponseDto(review);
    }

    @Override
    public ReviewResponseDto editReview(Long reviewId, Long userId, int rating, String comment) {
        Optional<Review> reviewOptional = reviewRepository.findById(reviewId);
        if (reviewOptional.isPresent()) {
            Review review = reviewOptional.get();
            if (!review.getUser().getId().equals(userId)) {
                throw new RuntimeException("User not authorized to edit this review");
            }
            review.setRating(rating);
            review.setComment(comment);
            reviewRepository.save(review);
            return toResponseDto(review);
        } else {
            throw new RuntimeException("Review not found");
        }
    }

    @Override
    public double getAverageRatingByBookId(Long bookId) {
        return reviewRepository.findAverageRatingByBookId(bookId);
    }

    @Override
    public Long countByBookId(Long bookId) {
        return reviewRepository.countByBookId(bookId);
    }
}
