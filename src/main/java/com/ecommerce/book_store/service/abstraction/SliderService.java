package com.ecommerce.book_store.service.abstraction;

import com.ecommerce.book_store.http.dto.request.implement.SliderRequestDto;
import com.ecommerce.book_store.http.dto.response.implement.SliderResponseDto;
import com.ecommerce.book_store.persistent.entity.Slider;

public interface SliderService extends IService<SliderRequestDto, SliderResponseDto, Slider>{
}
