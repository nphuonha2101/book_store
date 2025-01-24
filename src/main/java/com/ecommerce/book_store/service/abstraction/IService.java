package com.ecommerce.book_store.service.abstraction;

import com.ecommerce.book_store.http.dto.request.AbstractRequestDto;
import com.ecommerce.book_store.http.dto.response.AbstractResponseDto;
import com.ecommerce.book_store.persistent.entity.AbstractEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

public interface IService<RQ extends AbstractRequestDto, RS extends AbstractResponseDto, E extends AbstractEntity> {
    /**
     * Find entity by id
     *
     * @param id Id of the entity
     * @return Entity that has the id
     */
    @Transactional(readOnly = true)
    E findById(Long id);

    /**
     * Find all entities
     *
     * @return List of all entities
     */
    @Transactional(readOnly = true)
    List<E> findAll();

    /**
     * Find all entities and sort them. How to use: new Sort(Sort.Direction.ASC, "field1", "field2", ...)
     *
     * @param sort Sort object
     * @return List of all entities that are sorted
     */
    @Transactional(readOnly = true)
    List<E> findAll(Sort sort);

    /**
     * Find all entities and paginate them. How to use: PageRequest.of(page, size, sort)
     *
     * @param pageable Pageable object
     * @return List of entities that are paginated
     */
    @Transactional(readOnly = true)
    List<E> findAll(Pageable pageable);

    /**
     * Save requestDto to database
     *
     * @param requestDto RequestDto object get from client
     * @return ResponseDto object that is saved to database
     */
    @Transactional
    E save(RQ requestDto);

    /**
     * Update requestDto to database
     *
     * @param requestDto RequestDto object get from client
     * @param id         Id of the entity to update
     * @return ResponseDto object that is updated to database
     */
    @Transactional
    E update(RQ requestDto, Long id);

    /**
     * Delete entity by id
     *
     * @param id Id of the entity to delete
     * @return True if the entity is deleted, false otherwise
     */
    @Transactional
    boolean deleteById(Long id);


    /**
     * Convert requestDto to entity
     *
     * @param requestDto is a information from client and will be converted to entity to save to database
     * @return Entity object
     */
    E toEntity(RQ requestDto);

    /**
     * Convert entity to responseDto. The responseDto will be sent to client so we must convert entity to responseDto to hide sensitive information
     *
     * @param entity is a object get from database
     * @return ResponseDto object
     */
    RS toResponseDto(AbstractEntity entity);

    /**
     * Convert list of entities to list of responseDtos
     *
     * @param entities is a list of objects get from database
     * @return List of responseDtos
     */
    List<RS> toResponseDto(List<AbstractEntity> entities);

    /**
     * Copy properties from requestDto to entity. It is used in update method
     *
     * @param requestDto is an information from client
     * @param entity     is an object get from database
     */
    void copyProperties(RQ requestDto, E entity);

}
