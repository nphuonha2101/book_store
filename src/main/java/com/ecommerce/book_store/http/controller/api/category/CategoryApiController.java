package com.ecommerce.book_store.http.controller.api.category;

import com.ecommerce.book_store.http.ApiResponse;
import com.ecommerce.book_store.persistent.entity.Category;
import com.ecommerce.book_store.service.abstraction.CategoryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/categories")
@CrossOrigin(origins = "http://localhost:5173")
public class CategoryApiController {
    private final CategoryService categoryService;

    public CategoryApiController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping("/all")
    public ResponseEntity<ApiResponse<Object>> getAllCategories() {
        try {
            List<Category> categories = this.categoryService.findAll();
            return !categories.isEmpty() ? ApiResponse.success(categories, "Danh mục được lấy thành công") : ApiResponse.error("Không tìm thấy", HttpStatus.NOT_FOUND);

        } catch (Exception e) {
            return ApiResponse.error(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
