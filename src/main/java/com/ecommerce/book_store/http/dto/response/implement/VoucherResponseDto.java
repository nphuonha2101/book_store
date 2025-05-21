package com.ecommerce.book_store.http.dto.response.implement;

import com.ecommerce.book_store.http.dto.response.AbstractResponseDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class VoucherResponseDto extends AbstractResponseDto {
    private Long id;
    private String thumbnail;
    private String code;
    private Integer discount;
    private Integer minSpend;
    private LocalDateTime expiredDate;
    private List<CategoryResponseDto> categories;
}
