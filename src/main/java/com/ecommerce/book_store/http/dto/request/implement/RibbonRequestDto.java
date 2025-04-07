package com.ecommerce.book_store.http.dto.request.implement;

import com.ecommerce.book_store.http.dto.request.AbstractRequestDto;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class RibbonRequestDto extends AbstractRequestDto {
    private String name;
    private String description;
    private Boolean status;
    List<Long> bookIds;
}
