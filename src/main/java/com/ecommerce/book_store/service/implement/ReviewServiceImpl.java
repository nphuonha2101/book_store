package com.ecommerce.book_store.service.implement;

import com.ecommerce.book_store.http.dto.request.implement.ReviewRequestDto;
import com.ecommerce.book_store.http.dto.response.implement.BookResponseDto;
import com.ecommerce.book_store.http.dto.response.implement.ReviewResponseDto;
import com.ecommerce.book_store.http.dto.response.implement.UserResponseDto;
import com.ecommerce.book_store.persistent.entity.*;
import com.ecommerce.book_store.persistent.repository.abstraction.BookRepository;
import com.ecommerce.book_store.persistent.repository.abstraction.ReviewRepository;
import com.ecommerce.book_store.persistent.repository.abstraction.UserRepository;
import com.ecommerce.book_store.service.abstraction.BookService;
import com.ecommerce.book_store.service.abstraction.ReviewService;
import com.ecommerce.book_store.service.abstraction.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ReviewServiceImpl extends IServiceImpl<ReviewRequestDto, ReviewResponseDto, Review>
        implements ReviewService {
    private final ReviewRepository reviewRepository;
    private final BookService bookService;
    private final UserService userService;
    private final BookRepository bookRepository;
    private final UserRepository userRepository;

    @Autowired
    public ReviewServiceImpl(ReviewRepository repository, BookService bookService, UserService userService, BookRepository bookRepository, UserRepository userRepository) {
        super(repository);
        this.reviewRepository = repository;
        this.bookService = bookService;
        this.userService = userService;
        this.bookRepository = bookRepository;
        this.userRepository = userRepository;
    }
    @Override
    public Review toEntity(ReviewRequestDto requestDto) {
        Review result = new Review(
                null,
                null,
                requestDto.getRating(),
                requestDto.getComment()
        );
        User user = userService.findById(requestDto.getUserId());
        Book book = bookService.findById(requestDto.getBookId());
        result.setUser(user);
        result.setBook(book);
        return result;
    }
    @Override
    public ReviewResponseDto toResponseDto(AbstractEntity entity) {
        Review review = (Review) entity;
        UserResponseDto userResponseDto = new UserResponseDto(
                review.getUser().getId(),
                review.getUser().getName(),
                review.getUser().getEmail(),
                review.getUser().getAvatar(),
                review.getUser().getPhone(),
                review.getUser().getAddress(),
                null
                );
        BookResponseDto bookResponseDto = new BookResponseDto(
                review.getBook().getId(),
                review.getBook().getTitle(),
                review.getBook().getAuthorName(),
                review.getBook().getDescription(),
                review.getBook().getIsbn(),
                review.getBook().getImages(),
                review.getBook().getPrice(),
                review.getBook().getQuantity(),
                review.getBook().isAvailable(),
                null,
                review.getBook().getPublishedAt()
        );
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
        Optional<Book> bookOpt = bookRepository.findById(bookId);
        // TODO: Temporary solution
        Optional<User> userOpt = userRepository.findById(3L);
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
