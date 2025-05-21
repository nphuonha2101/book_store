package com.ecommerce.book_store.service.abstraction;

import com.ecommerce.book_store.http.dto.request.implement.RibbonItemRequestDto;
import com.ecommerce.book_store.http.dto.response.implement.RibbonItemResponseDto;
import com.ecommerce.book_store.persistent.entity.RibbonItem;

public interface RibbonItemService extends IService<RibbonItemRequestDto, RibbonItemResponseDto, RibbonItem> {
    boolean existsByRibbonIdAndBookId(Long ribbonId, Long bookId);
    boolean deleteByRibbonId(Long ribbonId);
}
