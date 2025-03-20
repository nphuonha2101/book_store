package com.ecommerce.book_store.http;

import lombok.Getter;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
public class ApiResponse<T> {
    private final boolean success;
    private final String message;
    private final int statusCode;
    private final T data;
    private final Map<String, Object> pagination;

    public ApiResponse(boolean success, String message, int statusCode, T data, Map<String, Object> pagination) {
        this.success = success;
        this.message = message;
        this.statusCode = statusCode;
        this.data = data;
        this.pagination = pagination;
    }

    public static <T> ResponseEntity<ApiResponse<T>> success(T data, String message) {
        return ResponseEntity.ok(new ApiResponse<>(true, message, HttpStatus.OK.value(), data, null));
    }

    public static <T> ResponseEntity<ApiResponse<List<T>>> successWithPagination(Page<T> page, String message) {
        Map<String, Object> pagination = getAttachPaginate(page);
        return ResponseEntity.ok(new ApiResponse<>(true, message, HttpStatus.OK.value(), page.getContent(), pagination));
    }

    public static <T> ResponseEntity<ApiResponse<T>> error(String message, HttpStatus status) {
        return ResponseEntity.status(status).body(new ApiResponse<>(false, message, status.value(), null, null));
    }

    public static Map<String, Object> getAttachPaginate(Page<?> page) {
        Map<String, Object> pagination = new HashMap<>();
        pagination.put("currentPage", page.getNumber());
        pagination.put("pageSize", page.getSize());
        pagination.put("totalPages", page.getTotalPages());
        pagination.put("totalElements", page.getTotalElements());
        pagination.put("isFirst", page.isFirst());
        pagination.put("isLast", page.isLast());
        return pagination;
    }
}
