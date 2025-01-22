package com.ecommerce.book_store.http.dto.response.implement;

import com.ecommerce.book_store.http.dto.response.AbstractResponseDto;
import lombok.Getter;

@Getter
public class RoleResponseDto extends AbstractResponseDto {
    private final Long id;
    private final String name;
    private final String description;

    public RoleResponseDto(Long id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }
}
