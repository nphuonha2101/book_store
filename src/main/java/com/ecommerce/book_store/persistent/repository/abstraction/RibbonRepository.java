package com.ecommerce.book_store.persistent.repository.abstraction;

import com.ecommerce.book_store.persistent.entity.Ribbon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RibbonRepository extends JpaRepository<Ribbon, Long> {
    List<Ribbon> findAllByStatusTrue();
}
