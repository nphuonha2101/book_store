package com.ecommerce.book_store.http.dto.request.implement;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class VoucherGetRequestDto {
    private List<Long> categoryIds;
    private Double minSpend;
}
