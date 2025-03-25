package com.ecommerce.book_store.http.dto.request.implement;

import com.ecommerce.book_store.http.dto.request.AbstractRequestDto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class UserRequestDto extends AbstractRequestDto {
    private String username;
    private String password;
    private String email;
    private MultipartFile avatar;
    private String phone;
    private String address;
    @Positive
    private Long roleId;
    public UserRequestDto () {

    }
}
