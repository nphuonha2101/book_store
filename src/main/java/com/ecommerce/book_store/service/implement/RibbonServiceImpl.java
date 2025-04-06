package com.ecommerce.book_store.service.implement;

import com.ecommerce.book_store.http.dto.request.implement.RibbonItemRequestDto;
import com.ecommerce.book_store.http.dto.request.implement.RibbonRequestDto;
import com.ecommerce.book_store.http.dto.response.implement.RibbonItemResponseDto;
import com.ecommerce.book_store.http.dto.response.implement.RibbonResponseDto;
import com.ecommerce.book_store.persistent.entity.AbstractEntity;
import com.ecommerce.book_store.persistent.entity.Ribbon;
import com.ecommerce.book_store.persistent.entity.RibbonItem;
import com.ecommerce.book_store.persistent.repository.abstraction.RibbonRepository;
import com.ecommerce.book_store.service.abstraction.RibbonItemService;
import com.ecommerce.book_store.service.abstraction.RibbonService;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RibbonServiceImpl extends IServiceImpl<RibbonRequestDto, RibbonResponseDto, Ribbon>
        implements RibbonService {
    private final RibbonItemService ribbonItemService;

    public RibbonServiceImpl(RibbonRepository repository, @Lazy RibbonItemService ribbonItemService) {
        super(repository);
        this.ribbonItemService = ribbonItemService;
    }

    @Override
    public List<Ribbon> findAllByStatusTrue() {
        return ((RibbonRepository) repository).findAllByStatusTrue();
    }

    @Override
    public Ribbon toEntity(RibbonRequestDto requestDto) {
        Ribbon entity = new Ribbon();

        entity.setName(requestDto.getName());
        entity.setDescription(requestDto.getDescription());
        entity.setStatus(requestDto.isStatus());

        List<RibbonItem> ribbonItems = requestDto.getRibbonItems().stream()
                .map(item -> {
                    RibbonItem ribbonItem = ribbonItemService.toEntity(item);
                    ribbonItem.setRibbon(entity);
                    return ribbonItem;
                })
                .toList();

        return entity;
    }

    @Override
    public RibbonResponseDto toResponseDto(AbstractEntity entity) {
        if (entity == null) {
            return null;
        }

        RibbonResponseDto responseDto = new RibbonResponseDto();
        Ribbon ribbon = (Ribbon) entity;

        responseDto.setId(ribbon.getId());
        responseDto.setName(ribbon.getName());
        responseDto.setDescription(ribbon.getDescription());
        responseDto.setStatus(ribbon.isStatus());

        List<RibbonItemResponseDto> ribbonItemResponseDtos = ribbon.getRibbonItems().stream()
                .map(ribbonItemService::toResponseDto)
                .toList();

        responseDto.setRibbonItems(ribbonItemResponseDtos);

        return responseDto;
    }

    @Override
    public void copyProperties(RibbonRequestDto requestDto, Ribbon entity) {
        if (requestDto.getName() != null) {
            entity.setName(requestDto.getName());
        }

        if (requestDto.getDescription() != null) {
            entity.setDescription(requestDto.getDescription());
        }

        if (requestDto.getRibbonItems() != null) {
            List<RibbonItem> ribbonItems = requestDto.getRibbonItems().stream()
                    .map(item -> {
                        RibbonItem ribbonItem = ribbonItemService.toEntity(item);
                        ribbonItem.setRibbon(entity);
                        return ribbonItem;
                    })
                    .toList();
            entity.setRibbonItems(ribbonItems);
        }
    }
}
