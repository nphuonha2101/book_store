package com.ecommerce.book_store.service.abstraction;

import com.ecommerce.book_store.http.dto.request.AbstractRequestDto;
import com.ecommerce.book_store.http.dto.response.AbstractResponseDto;
import com.ecommerce.book_store.persistent.entity.AbstractEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

public interface IAdvancedService<RQ extends AbstractRequestDto, RS extends AbstractResponseDto, E extends AbstractEntity>
        extends IService<RQ, RS, E> {
    /**
     * Find all entities by criteria. The criteria is a map of key-value pairs.
     * The key is the field name of the entity and the value is the value of the field.
     * This is a AdvancedRepository method. If the repository does not implement this method, it will throw an exception.
     *
     * @param criteria Map of criteria
     * @param pageable Pageable object
     * @return List of entities that match the criteria
     */
    @Transactional(readOnly = true)
    List<RS> findAllByCriteria(Map<String, Object> criteria, Pageable pageable);

    /**
     * Find all deleted entities. This is a AdvancedRepository method. If the repository does not implement this method, it will throw an exception.
     *
     * @param pageable Pageable object
     * @return List of deleted entities
     */
    @Transactional(readOnly = true)
    List<RS> findAllDeleted(Pageable pageable);

    /**
     * Find all deleted entities by criteria. This is a AdvancedRepository method. If the repository does not implement this method, it will throw an exception.
     *
     * @param criteria Map of criteria
     * @param pageable Pageable object
     * @return List of deleted entities that match the criteria
     */
    @Transactional(readOnly = true)
    List<RS> findAllDeletedByCriteria(Map<String, Object> criteria, Pageable pageable);

    /**
     * Delete entity by id. This is a AdvancedRepository method. If the repository does not implement this method, it will throw an exception.
     *
     * @param ids A list of ids of the entities to delete
     * @return Number of entities deleted
     */
    @Transactional
    int deleteByIds(List<Long> ids);

    /**
     * Soft delete entity by id. This is a AdvancedRepository method. If the repository does not implement this method, it will throw an exception.
     *
     * @param id Id of the entity to soft delete
     * @return True if the entity is soft deleted, false otherwise
     */
    @Transactional
    boolean softDeleteById(Long id);

    /**
     * Soft delete entities by ids. This is a AdvancedRepository method. If the repository does not implement this method, it will throw an exception.
     *
     * @param ids A list of ids of the entities to soft delete
     * @return Number of entities soft deleted
     */
    @Transactional
    int softDeleteByIds(List<Long> ids);

    /**
     * Restore entities by ids. This is a AdvancedRepository method. If the repository does not implement this method, it will throw an exception.
     *
     * @param ids A list of ids of the entities to restore
     * @return Number of entities restored
     */
    @Transactional
    int restoreByIds(List<Long> ids);

}
