package com.ecommerce.book_store.service.implement;

import com.ecommerce.book_store.http.dto.request.AbstractRequestDto;
import com.ecommerce.book_store.http.dto.response.AbstractResponseDto;
import com.ecommerce.book_store.persistent.entity.AbstractEntity;
import com.ecommerce.book_store.service.abstraction.IService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public abstract class IServiceImpl<RQ extends AbstractRequestDto, RS extends AbstractResponseDto, E extends AbstractEntity> implements IService<RQ, RS, E> {
    protected JpaRepository<E, Long> repository;

    public IServiceImpl(JpaRepository<E, Long> repository) {
        this.repository = repository;
    }

    @Override
    public E findById(Long id) {
        return repository.findById(id).orElse(null);
    }

    @Override
    public List<E> findAll() {
        return repository.findAll();
    }

    @Override
    public List<E> findAll(Sort sort) {
        return repository.findAll(sort);
    }

    @Override
    public Page<E> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Override
    public E save(RQ requestDto) {
        E entity = toEntity(requestDto);
        return repository.save(entity);
    }

    @Override
    public E update(RQ requestDto, Long id) {
        E entity = repository.findById(id).orElse(null);
        if (entity == null) {
            return null;
        }
        this.copyProperties(requestDto, entity);

        repository.save(entity);
        return entity;
    }

    @Override
    public boolean deleteById(Long id) {
        if (!repository.existsById(id)) {
            return false;
        }
        repository.deleteById(id);
        return true;
    }

    @Override
    public List<RS> toResponseDto(List<AbstractEntity> entities) {
        return entities.stream().map(this::toResponseDto).collect(Collectors.toList());
    }
}
