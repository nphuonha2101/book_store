package com.ecommerce.book_store.persistent.repository.abstraction;

import com.ecommerce.book_store.persistent.entity.AbstractEntity;

import java.util.List;
import java.util.Map;

public interface IRepository <E extends AbstractEntity>
{
    List<E> findAll();
    List<E> findAllByCriteria(Map<String, Object> criteria);
    List<E> findAllDeleted();
    List<E> findAllDeletedByCriteria(Map<String, Object> criteria);
    E findById(int id);
    E save(E entity);
    E update(E entity);
    boolean deleteById(int id);
    boolean deleteByIds(List<Long> ids);
    boolean forceDeleteById(int id);

}
