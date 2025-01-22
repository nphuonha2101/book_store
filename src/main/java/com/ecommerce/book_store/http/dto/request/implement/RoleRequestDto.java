package com.ecommerce.book_store.http.dto.request.implement;

import com.ecommerce.book_store.http.dto.request.AbstractRequestDto;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RoleRequestDto extends AbstractRequestDto {
    @NotBlank
    private String name;
    @NotBlank
    private String description;

    public RoleRequestDto() {
    }

    public RoleRequestDto(String name, String description) {
        this.name = name;
        this.description = description;
    }

}
