package com.ecommerce.book_store.persistent.repository.implement;

import com.ecommerce.book_store.core.exception.RepositoryException;
import com.ecommerce.book_store.persistent.entity.AbstractEntity;
import com.ecommerce.book_store.persistent.repository.abstraction.IAdvancedRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

public abstract class IAdvancedRepositoryImpl<E extends AbstractEntity> implements IAdvancedRepository<E> {
    private final Class<E> entityClass;

    @PersistenceContext
    private EntityManager entityManager;

    public IAdvancedRepositoryImpl(Class<E> entityClass) {
        this.entityClass = entityClass;
    }


    @Override
    public List<E> findAllByCriteria(Map<String, Object> criteria, Pageable pageable) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<E> criteriaQuery = criteriaBuilder.createQuery(entityClass);
        Root<E> root = criteriaQuery.from(entityClass);
        criteriaQuery.select(root);

        criteria.forEach((key, value) -> {
            if (value != null)
                criteriaQuery.where(criteriaBuilder.equal(root.get(key), value));
        });

        if (pageable == null) {
            return entityManager.createQuery(criteriaQuery).getResultList();
        }

        if (pageable.getSort().isSorted()) {
            pageable.getSort().get().forEach(order -> {
                if (order.getDirection().isAscending()) {
                    criteriaQuery.orderBy(criteriaBuilder.asc(root.get(order.getProperty())));
                } else {
                    criteriaQuery.orderBy(criteriaBuilder.desc(root.get(order.getProperty())));
                }
            });
        }
        return entityManager.createQuery(criteriaQuery).setFirstResult((int) pageable.getOffset()).setMaxResults(pageable.getPageSize()).getResultList();
    }


    @Override
    public List<E> findAllDeleted(Pageable pageable) {
        TypedQuery<E> query = entityManager.createQuery("SELECT e FROM " + entityClass.getName() + " e WHERE e.deletedAt IS NOT NULL", entityClass);

        if (pageable == null) {
            return query.getResultList();
        }

        return query.setFirstResult((int) pageable.getOffset()).setMaxResults(pageable.getPageSize()).getResultList();
    }

    @Override
    public List<E> findAllDeletedByCriteria(Map<String, Object> criteria, Pageable pageable) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<E> criteriaQuery = criteriaBuilder.createQuery(entityClass);
        Root<E> root = criteriaQuery.from(entityClass);
        criteriaQuery.select(root);

        criteria.forEach((key, value) -> {
            if (value != null)
                criteriaQuery.where(criteriaBuilder.equal(root.get(key), value));
        });
        return entityManager.createQuery(criteriaQuery).getResultList();
    }


    @Override
    public int deleteByIds(List<Long> ids) {
        try {
            TypedQuery<E> query = entityManager.createQuery("DELETE FROM " + entityClass.getName() + " e WHERE e.id IN :ids", entityClass);
            query.setParameter("ids", ids);
            return query.executeUpdate();
        } catch (Exception e) {
            throw new RepositoryException("Error while deleting entities by ids. Entity: " + entityClass.getName() + " ids: " + ids);
        }
    }

    @Override
    public int softDeleteByIds(List<Long> ids) {
        TypedQuery<E> query = entityManager.createQuery("UPDATE " + entityClass.getName() + " e SET e.deletedAt = CURRENT_TIMESTAMP WHERE e.id IN :ids", entityClass);
        query.setParameter("ids", ids);
        return query.executeUpdate();
    }

    @Override
    public int restoreByIds(List<Long> ids) {
        TypedQuery<E> query = entityManager.createQuery("UPDATE " + entityClass.getName() + " e SET e.deletedAt = NULL WHERE e.id IN :ids", entityClass);
        query.setParameter("ids", ids);
        return query.executeUpdate();
    }

    @Override
    public boolean softDeleteById(Long id) {
        try {
            TypedQuery<E> query = entityManager.createQuery("UPDATE " + entityClass.getName() + " e SET e.deletedAt = CURRENT_TIMESTAMP WHERE e.id = :id", entityClass);
            query.setParameter("id", id);
            return query.executeUpdate() > 0;
        } catch (Exception e) {
            throw new RepositoryException("Error while soft deleting entity by id. Entity: " + entityClass.getName() + " id: " + id);
        }
    }

}
