package com.ecommerce.book_store.persistent.repository.abstraction;

import com.ecommerce.book_store.persistent.entity.RibbonItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RibbonItemRepository extends JpaRepository<RibbonItem, Long> {
    boolean existsByRibbonIdAndBookId(Long ribbonId, Long bookId);
}
