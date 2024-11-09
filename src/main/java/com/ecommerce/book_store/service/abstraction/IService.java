package com.ecommerce.book_store.service.abstraction;

import com.ecommerce.book_store.persistent.entity.AbstractEntity;
import io.lettuce.core.GeoArgs;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Map;

public interface IService<T extends AbstractEntity> {
    T findById(Long id);
    List<T> findAll();
    List<T> findAll(Sort sort);
    List<T> findAll(Pageable pageable);
    List<T> findAllByCriteria(Map<String, Object> criteria, Pageable pageable);
    List<T> findAllDeleted(Pageable pageable);
    List<T> findAllDeletedByCriteria(Map<String, Object> criteria, Pageable pageable);
    T save(T T);
    T update(T T);
    boolean deleteById(Long id);
    int deleteByIds(List<Long> ids);
    boolean softDeleteById(Long id);
    int softDeleteByIds(List<Long> ids);
    int restoreByIds(List<Long> ids);

}
