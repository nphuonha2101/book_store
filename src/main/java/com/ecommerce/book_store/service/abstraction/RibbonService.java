package com.ecommerce.book_store.service.abstraction;

import com.ecommerce.book_store.http.dto.request.implement.RibbonRequestDto;
import com.ecommerce.book_store.http.dto.response.implement.RibbonResponseDto;
import com.ecommerce.book_store.persistent.entity.Ribbon;

import java.util.List;

public interface RibbonService extends IService<RibbonRequestDto, RibbonResponseDto, Ribbon> {
    List<RibbonResponseDto> findAllByStatusTrue();
    void saveRibbonWithRibbonItems(RibbonRequestDto requestDto);
    void updateRibbonWithRibbonItems(RibbonRequestDto requestDto, Long id);
}
