package com.ecommerce.book_store.http.dto.response.implement;

import com.ecommerce.book_store.http.dto.response.AbstractResponseDto;
import lombok.Getter;

@Getter
public class SliderResponseDto extends AbstractResponseDto {
    private final Long id;
    private final String title;
    private final String description;
    private final String image;
    private final String url;

    public SliderResponseDto(Long id, String title, String description, String image, String url) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.image = image;
        this.url = url;
    }
}
