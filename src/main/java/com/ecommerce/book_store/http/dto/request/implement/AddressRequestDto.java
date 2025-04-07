package com.ecommerce.book_store.http.dto.request.implement;

import com.ecommerce.book_store.http.dto.request.AbstractRequestDto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddressRequestDto extends AbstractRequestDto {
    private Long userId;
    private String fullName;
    private String phone;
    private String province;
    private String district;
    private String ward;
    private String addInfo;
    private Boolean isDefault;


}
