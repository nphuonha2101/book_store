package com.ecommerce.book_store.persistent.repository.abstraction;


import com.ecommerce.book_store.persistent.entity.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    Page<Review> findByBookId(Long bookId, Pageable pageable);

    @Query("SELECT CASE WHEN COUNT (r) > 0 THEN true ELSE false END " +
           "FROM Review r WHERE r.user.id = :userId AND r.book.id = :bookId")
    boolean isUserReviewedBook(Long userId, Long bookId);
    @Query("SELECT COALESCE(AVG(r.rating), 0) FROM Review r WHERE r.book.id = :bookId")
    double findAverageRatingByBookId(Long bookId);
    Long countByBookId(Long bookId);
}
