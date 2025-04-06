package com.ecommerce.book_store.service.implement;

import com.ecommerce.book_store.http.dto.request.implement.ReviewRequestDto;
import com.ecommerce.book_store.http.dto.response.implement.BookResponseDto;
import com.ecommerce.book_store.http.dto.response.implement.ReviewResponseDto;
import com.ecommerce.book_store.http.dto.response.implement.UserResponseDto;
import com.ecommerce.book_store.persistent.entity.*;
import com.ecommerce.book_store.persistent.repository.abstraction.ReviewRepository;
import com.ecommerce.book_store.service.abstraction.*;
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
                review.getComment()
        );
    }

    @Override
    public void copyProperties(ReviewRequestDto requestDto, Review entity) {
        entity.setRating(requestDto.getRating());
        entity.setComment(requestDto.getComment());
    }

    @Override
    public List<Review> getReviewsByBookId(Long bookId) {
        return reviewRepository.findByBookId(bookId);
    }

    @Override
    public Review addReview(Long bookId, Long userId, int rating, String comment) {
        Optional<Book> bookOpt = bookService.getRepository().findById(bookId);
        // TODO: Temporary solution
        Optional<User> userOpt = userService.getRepository().findById(3L);
        if (bookOpt.isPresent() && userOpt.isPresent()) {
            Review review = new Review();
            review.setBook(bookOpt.get());
            review.setUser(userOpt.get());
            review.setRating(rating);
            review.setComment(comment);
            return reviewRepository.save(review);
        }
        return null;
    }

    @Override
    public double getAverageRatingByBookId(Long bookId) {
        List<Review> reviews = reviewRepository.findByBookId(bookId);
        return reviews.stream()
                .mapToInt(Review::getRating)
                .average()
                .orElse(0.0);
    }
}
