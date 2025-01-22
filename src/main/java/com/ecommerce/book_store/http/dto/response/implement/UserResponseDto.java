package com.ecommerce.book_store.http.dto.response.implement;

import com.ecommerce.book_store.http.dto.response.AbstractResponseDto;
import lombok.Getter;

@Getter
public class UserResponseDto extends AbstractResponseDto {
    private final Long id;
    private final String name;
    private final String email;
    private final String avatar;
    private final String phone;
    private final String address;
    private final RoleResponseDto role;

    public UserResponseDto(Long id, String name, String email, String avatar, String phone, String address, RoleResponseDto role) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.avatar = avatar;
        this.phone = phone;
        this.address = address;
        this.role = role;
    }
}
