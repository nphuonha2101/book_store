package com.ecommerce.book_store.persistent.repository.abstraction;

import com.ecommerce.book_store.persistent.entity.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
    Page<Book> findByTitleContaining(String title, Pageable pageable);
    Page<Book> findByTitleIn(List<String> titles, Pageable pageable);
    @Query("SELECT b FROM Book b WHERE " +
            "(:authorName IS NULL OR :authorName = '' OR b.authorName LIKE %:authorName%) AND " +
            "(:title IS NULL OR :title = '' OR b.title LIKE %:title%) AND " +
            "(:categoryIds IS NULL OR b.category.id IN :categoryIds) AND " +
            "(:minPrice IS NULL OR b.price >= :minPrice) AND " +
            "(:maxPrice IS NULL OR b.price <= :maxPrice)")
    Page<Book> filter(String authorName, String title, List<Long> categoryIds, Double minPrice, Double maxPrice, Pageable pageable);
}
