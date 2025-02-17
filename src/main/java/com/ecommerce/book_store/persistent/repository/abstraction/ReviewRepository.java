package com.ecommerce.book_store.persistent.repository.abstraction;


import com.ecommerce.book_store.persistent.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByBookId(Long bookId);

}
