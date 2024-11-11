package com.ecommerce.book_store.persistent.repository.implement;

import com.ecommerce.book_store.persistent.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepositoryImpl extends JpaRepository<Category, Long> {
}
