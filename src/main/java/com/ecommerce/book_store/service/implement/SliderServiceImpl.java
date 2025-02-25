package com.ecommerce.book_store.service.implement;

import com.ecommerce.book_store.http.dto.request.implement.SliderRequestDto;
import com.ecommerce.book_store.http.dto.response.implement.CategoryResponseDto;
import com.ecommerce.book_store.http.dto.response.implement.SliderResponseDto;
import com.ecommerce.book_store.persistent.entity.AbstractEntity;
import com.ecommerce.book_store.persistent.entity.Slider;
import com.ecommerce.book_store.service.abstraction.SliderService;
import org.springframework.data.jpa.repository.JpaRepository;

public class SliderServiceImpl extends IServiceImpl<SliderRequestDto, SliderResponseDto, Slider> implements SliderService {

    public SliderServiceImpl(JpaRepository<Slider, Long> repository) {
        super(repository);
    }

    @Override
    public Slider toEntity(SliderRequestDto requestDto) {
        Slider slider = new Slider();
        slider.setTitle(requestDto.getTitle());
        slider.setDescription(requestDto.getDescription());
        slider.setImage(requestDto.getImage());
        slider.setUrl(requestDto.getUrl());
        return slider;
    }

    @Override
    public SliderResponseDto toResponseDto(AbstractEntity entity) {
        Slider slider = (Slider) entity;
        return new SliderResponseDto(slider.getId(), slider.getTitle(), slider.getDescription(), slider.getImage(), slider.getUrl());
    }

    @Override
    public void copyProperties(SliderRequestDto requestDto, Slider entity) {
        entity.setTitle(requestDto.getTitle());
        entity.setDescription(requestDto.getDescription());
        entity.setImage(requestDto.getImage());
        entity.setUrl(requestDto.getUrl());

    }
}
