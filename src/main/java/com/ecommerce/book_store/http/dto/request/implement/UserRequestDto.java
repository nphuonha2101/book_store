package com.ecommerce.book_store.http.dto.request.implement;

import com.ecommerce.book_store.http.dto.request.AbstractRequestDto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserRequestDto extends AbstractRequestDto {
    @NotBlank
    private String username;
    @NotBlank
    private String email;
    @NotBlank
    private String password;
    @NotBlank
    private String avatar;
    @NotBlank
    private String phone;
    @NotBlank
    private String address;
    @Positive
    private Long roleId;
    public UserRequestDto () {

    }

    public UserRequestDto(String username, String email, String password, String avatar, String phone, String address, Long roleId) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.avatar = avatar;
        this.phone = phone;
        this.address = address;
        this.roleId = roleId;
    }
}
