package com.ecommerce.book_store.http.dto.response.implement;

import com.ecommerce.book_store.http.dto.response.AbstractResponseDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@AllArgsConstructor
@Setter
public class AddressResponseDto extends AbstractResponseDto {
    private Long id;
    private Long userId;
    private String fullName;
    private String phone;
    private String province;
    private String district;
    private String ward;
    private String addInfo;
    private Boolean isDefault;



}
