package com.ecommerce.book_store.persistent.repository.implement;

import com.ecommerce.book_store.core.exception.RepositoryException;
import com.ecommerce.book_store.persistent.entity.AbstractEntity;
import com.ecommerce.book_store.persistent.repository.abstraction.IRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Map;

public abstract class Repository<E extends AbstractEntity> implements IRepository<E> {
    private final Class<E> entityClass;

    @PersistenceContext
    private EntityManager entityManager;

    public Repository(Class<E> entityClass) {
        this.entityClass = entityClass;
    }


    @Override

    public List<E> findAll() {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<E> criteriaQuery = criteriaBuilder.createQuery(entityClass);
        Root<E> root = criteriaQuery.from(entityClass);
        criteriaQuery.select(root);

        return entityManager.createQuery(criteriaQuery).getResultList();
    }

    @Override
    public List<E> findAllByCriteria(Map<String, Object> criteria) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<E> criteriaQuery = criteriaBuilder.createQuery(entityClass);
        Root<E> root = criteriaQuery.from(entityClass);
        criteriaQuery.select(root);

        int offset = criteria.get("offset") != null ? (int) criteria.get("offset") : 0;
        int limit = criteria.get("limit") != null ? (int) criteria.get("limit") : 10;

        criteria.remove("offset");
        criteria.remove("limit");

        criteria.forEach((key, value) -> {
            if (value != null)
                criteriaQuery.where(criteriaBuilder.equal(root.get(key), value));
        });

        return entityManager.createQuery(criteriaQuery).setFirstResult(offset).setMaxResults(limit).getResultList();
    }

    @Override
    public E findById(int id) {
        try {
            return entityManager.find(entityClass, id);
        } catch (Exception e) {
            throw new RepositoryException("Error while finding entity by id. Entity: " + entityClass.getName() + " id: " + id);
        }
    }

    @Override
    public E save(E entity) {
        try {
            entityManager.persist(entity);
            return entity;
        } catch (Exception e) {
            throw new RepositoryException("Error while saving entity. Entity: " + entityClass.getName());
        }
    }

    @Override
    public E update(E entity) {
        try {

            entityManager.merge(entity);
            return entity;
        } catch (Exception e) {
            throw new RepositoryException("Error while updating entity. Entity: " + entityClass.getName());
        }
    }

    @Override
    public boolean deleteById(int id) {
        try {
            E entity = entityManager.find(entityClass, id);
            entityManager.remove(entity);
            return true;
        } catch (Exception e) {
            throw new RepositoryException("Error while deleting entity by id. Entity: " + entityClass.getName() + " id: " + id);
        }
    }

    @Override
    public boolean deleteByIds(List<Long> ids) {
        try {
            for (Long id : ids) {
                E entity = entityManager.find(entityClass, id);
                entityManager.remove(entity);
            }
            return true;
        } catch (Exception e) {
            throw new RepositoryException("Error while deleting entities by ids. Entity: " + entityClass.getName() + " ids: " + ids);
        }
    }
}
