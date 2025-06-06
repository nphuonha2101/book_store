package com.ecommerce.book_store.service.implement;

import com.ecommerce.book_store.http.dto.request.implement.CategoryRequestDto;
import com.ecommerce.book_store.http.dto.response.implement.CategoryResponseDto;
import com.ecommerce.book_store.persistent.entity.AbstractEntity;
import com.ecommerce.book_store.persistent.entity.Category;
import com.ecommerce.book_store.persistent.repository.abstraction.CategoryRepository;
import com.ecommerce.book_store.service.abstraction.CategoryService;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl extends IServiceImpl<CategoryRequestDto, CategoryResponseDto, Category>
        implements CategoryService {
    public CategoryServiceImpl(CategoryRepository repository) {
        super(repository);
    }

    @Override
    public Category toEntity(CategoryRequestDto requestDto) {
        Category category = new Category();
        category.setName(requestDto.getName());
        category.setDescription(requestDto.getDescription());
        return category;
    }

    @Override
    public CategoryResponseDto toResponseDto(AbstractEntity entity) {
        if (entity == null) {
            return null;
        }
        Category category = (Category) entity;
        return new CategoryResponseDto(category.getId(), category.getName(), category.getDescription());
    }

    @Override
    public void copyProperties(CategoryRequestDto requestDto, Category entity) {
        entity.setName(requestDto.getName());
        entity.setDescription(requestDto.getDescription());
    }
}
