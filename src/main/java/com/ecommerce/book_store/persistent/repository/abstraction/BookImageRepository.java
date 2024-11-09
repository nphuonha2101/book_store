package com.ecommerce.book_store.persistent.repository.abstraction;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookImageRepository extends JpaRepository<BookImageRepository, Long> {
}
