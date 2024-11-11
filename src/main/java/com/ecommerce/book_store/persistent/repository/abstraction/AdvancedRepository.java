package com.ecommerce.book_store.persistent.repository.abstraction;

import com.ecommerce.book_store.persistent.entity.AbstractEntity;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

public interface AdvancedRepository<E extends AbstractEntity>
{
    List<E> findAllByCriteria(Map<String, Object> criteria, Pageable pageable);
    List<E> findAllDeleted(Pageable pageable);
    List<E> findAllDeletedByCriteria(Map<String, Object> criteria, Pageable pageable);
    int deleteByIds(List<Long> ids);
    int softDeleteByIds(List<Long> ids);
    int restoreByIds(List<Long> ids);
    boolean softDeleteById(Long id);

}
