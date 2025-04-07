package com.ecommerce.book_store.persistent.repository.abstraction;

import com.ecommerce.book_store.persistent.entity.Ribbon;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RibbonRepository extends JpaRepository<Ribbon, Long> {
    Page<Ribbon> findAllByStatusTrue(Pageable pageable);
    List<Ribbon> findAllByStatusTrue();
}
