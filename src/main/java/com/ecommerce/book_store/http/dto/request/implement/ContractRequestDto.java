package com.ecommerce.book_store.http.dto.request.implement;

import com.ecommerce.book_store.http.dto.request.AbstractRequestDto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ContractRequestDto extends AbstractRequestDto {
    private String email;
    private String message;

    public ContractRequestDto() {
    }
}
