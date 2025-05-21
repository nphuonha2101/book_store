package com.ecommerce.book_store.http.controller.api.category;

import com.ecommerce.book_store.http.ApiResponse;
import com.ecommerce.book_store.http.dto.response.implement.CategoryResponseDto;
import com.ecommerce.book_store.service.abstraction.CategoryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/categories")
public class CategoryApiController {
    private final CategoryService categoryService;

    public CategoryApiController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping("/all")
    public ResponseEntity<ApiResponse<List<CategoryResponseDto>>> getAllCategories() {
        try {
            List<CategoryResponseDto> categories = this.categoryService.findAll();
            return !categories.isEmpty() ? ApiResponse.success(categories, "Danh mục được lấy thành công") : ApiResponse.error("Không tìm thấy", HttpStatus.NOT_FOUND);

        } catch (Exception e) {
            return ApiResponse.error(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
