package com.ecommerce.book_store.http.dto.request.implement;

import com.ecommerce.book_store.http.dto.request.AbstractRequestDto;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class SliderRequestDto extends AbstractRequestDto {
    private String title;
    private String description;
    private String image;
    private String url;
}
