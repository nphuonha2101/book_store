package com.ecommerce.book_store.service.implement;

import com.ecommerce.book_store.http.dto.request.AbstractRequestDto;
import com.ecommerce.book_store.http.dto.response.AbstractResponseDto;
import com.ecommerce.book_store.persistent.entity.AbstractEntity;
import com.ecommerce.book_store.persistent.repository.abstraction.AdvancedRepository;
import com.ecommerce.book_store.service.abstraction.IAdvancedService;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Map;

public abstract class IAdvancedServiceImpl<RS extends AbstractRequestDto, RQ extends AbstractResponseDto, E extends AbstractEntity>
        extends IServiceImpl<RS, RQ, E>
        implements IAdvancedService<RS , RQ , E > {

    protected AdvancedRepository<E> advancedRepository;

    public IAdvancedServiceImpl(JpaRepository<E, Long> repository, AdvancedRepository<E> advancedRepository) {
        super(repository);
        this.advancedRepository = advancedRepository;
    }

    @Override
    public List<RQ> findAllByCriteria(Map<String, Object> criteria, Pageable pageable) {
        return advancedRepository.findAllByCriteria(criteria, pageable).stream().map(this::toResponseDto).toList();
    }

    @Override
    public List<RQ> findAllDeleted(Pageable pageable) {
        return advancedRepository.findAllDeleted(pageable).stream().map(this::toResponseDto).toList();
    }

    @Override
    public List<RQ> findAllDeletedByCriteria(Map<String, Object> criteria, Pageable pageable) {
        return advancedRepository.findAllDeletedByCriteria(criteria, pageable).stream().map(this::toResponseDto).toList();
    }

    @Override
    public int deleteByIds(List<Long> ids) {
        return advancedRepository.deleteByIds(ids);
    }

    @Override
    public boolean softDeleteById(Long id) {
        return advancedRepository.softDeleteById(id);
    }

    @Override
    public int softDeleteByIds(List<Long> ids) {
        return advancedRepository.softDeleteByIds(ids);
    }

    @Override
    public int restoreByIds(List<Long> ids) {
        return advancedRepository.restoreByIds(ids);
    }

    @Override
    public abstract E toEntity(RS requestDto);

    @Override
    public abstract RQ toResponseDto(AbstractEntity entity);

    @Override
    public abstract List<RQ> toResponseDto(List<AbstractEntity> entities);

    @Override
    public abstract void copyProperties(RS requestDto, E entity);
}
