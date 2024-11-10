package com.ecommerce.book_store.service.abstraction;

import com.ecommerce.book_store.persistent.entity.AbstractEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

public interface IService<T extends AbstractEntity> {
    @Transactional(readOnly = true)
    T findById(Long id);
    @Transactional(readOnly = true)
    List<T> findAll();
    @Transactional(readOnly = true)
    List<T> findAll(Sort sort);
    @Transactional(readOnly = true)
    List<T> findAll(Pageable pageable);
    @Transactional(readOnly = true)
    List<T> findAllByCriteria(Map<String, Object> criteria, Pageable pageable);
    @Transactional(readOnly = true)
    List<T> findAllDeleted(Pageable pageable);
    @Transactional(readOnly = true)
    List<T> findAllDeletedByCriteria(Map<String, Object> criteria, Pageable pageable);
    @Transactional
    T save(T T);
    @Transactional
    T update(T T);
    @Transactional
    boolean deleteById(Long id);
    @Transactional
    int deleteByIds(List<Long> ids);
    @Transactional
    boolean softDeleteById(Long id);
    @Transactional
    int softDeleteByIds(List<Long> ids);
    @Transactional
    int restoreByIds(List<Long> ids);

}
