package com.ecommerce.book_store.service.abstraction;

import com.ecommerce.book_store.http.dto.request.implement.CategoryRequestDto;
import com.ecommerce.book_store.http.dto.response.implement.CategoryResponseDto;
import com.ecommerce.book_store.persistent.entity.Category;

public interface CategoryService extends IService<CategoryRequestDto, CategoryResponseDto, Category> {
}
