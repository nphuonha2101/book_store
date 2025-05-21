package com.ecommerce.book_store.service.implement;

import com.ecommerce.book_store.http.dto.request.AbstractRequestDto;
import com.ecommerce.book_store.http.dto.response.AbstractResponseDto;
import com.ecommerce.book_store.persistent.entity.AbstractEntity;
import com.ecommerce.book_store.service.abstraction.IService;
import lombok.Getter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Getter
public abstract class IServiceImpl<RQ extends AbstractRequestDto, RS extends AbstractResponseDto, E extends AbstractEntity> implements IService<RQ, RS, E> {
    protected JpaRepository<E, Long> repository;

    public IServiceImpl(JpaRepository<E, Long> repository) {
        this.repository = repository;
    }

    @Override
    public RS findById(Long id) {
        return repository.findById(id).map(this::toResponseDto).orElse(null);
    }

    @Override
    public List<RS> findAll() {
        return repository.findAll().stream().map(this::toResponseDto).collect(Collectors.toList());
    }

    @Override
    public List<RS> findAll(Sort sort) {
        return repository.findAll(sort).stream().map(this::toResponseDto).collect(Collectors.toList());
    }

    @Override
    public Page<RS> findAll(Pageable pageable) {
        return repository.findAll(pageable).map(this::toResponseDto);
    }

    @Override
    public RS save(RQ requestDto) {
        E entity = toEntity(requestDto);
        return toResponseDto(repository.save(entity));
    }

    @Override
    public RS update(RQ requestDto, Long id) {
        E entity = repository.findById(id).orElse(null);
        if (entity == null) {
            return null;
        }
        this.copyProperties(requestDto, entity);

        repository.save(entity);
        return toResponseDto(entity);
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
