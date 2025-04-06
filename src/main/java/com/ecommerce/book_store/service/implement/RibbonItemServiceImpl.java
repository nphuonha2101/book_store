package com.ecommerce.book_store.service.implement;

import com.ecommerce.book_store.http.dto.request.implement.RibbonItemRequestDto;
import com.ecommerce.book_store.http.dto.response.implement.RibbonItemResponseDto;
import com.ecommerce.book_store.persistent.entity.AbstractEntity;
import com.ecommerce.book_store.persistent.entity.Book;
import com.ecommerce.book_store.persistent.entity.Ribbon;
import com.ecommerce.book_store.persistent.entity.RibbonItem;
import com.ecommerce.book_store.persistent.repository.abstraction.RibbonItemRepository;
import com.ecommerce.book_store.service.abstraction.BookService;
import com.ecommerce.book_store.service.abstraction.RibbonItemService;
import com.ecommerce.book_store.service.abstraction.RibbonService;
import org.springframework.stereotype.Service;

@Service
public class RibbonItemServiceImpl extends IServiceImpl<RibbonItemRequestDto, RibbonItemResponseDto, RibbonItem>
        implements RibbonItemService {
    private final RibbonService ribbonService;
    private final BookService bookService;
    public RibbonItemServiceImpl(RibbonItemRepository repository, RibbonService ribbonService, BookService bookService) {
        super(repository);
        this.ribbonService = ribbonService;
        this.bookService = bookService;
    }

    @Override
    public boolean existsByRibbonIdAndBookId(Long ribbonId, Long bookId) {
        return ((RibbonItemRepository) repository).existsByRibbonIdAndBookId(ribbonId, bookId);
    }

    @Override
    public RibbonItem toEntity(RibbonItemRequestDto requestDto) {
        RibbonItem entity = new RibbonItem();
        Ribbon ribbon = ribbonService.getRepository().findById(requestDto.getRibbonId()).orElseThrow(
                () -> new RuntimeException("Ribbon not found")
        );
        Book book = bookService.getRepository().findById(requestDto.getBookId()).orElseThrow(
                () -> new RuntimeException("Book not found")
        );
        entity.setRibbon(ribbon);
        entity.setBook(book);
        entity.setRibbonId(requestDto.getRibbonId());
        entity.setBookId(requestDto.getBookId());
        return entity;
    }

    @Override
    public RibbonItemResponseDto toResponseDto(AbstractEntity entity) {
        if (entity == null) {
            return null;
        }
        RibbonItem ribbonItem = (RibbonItem) entity;
        RibbonItemResponseDto responseDto = new RibbonItemResponseDto();

        responseDto.setId(ribbonItem.getId());
        responseDto.setRibbonId(ribbonItem.getRibbonId());
        responseDto.setBook(bookService.toResponseDto(ribbonItem.getBook()));
        return responseDto;
    }

    @Override
    public void copyProperties(RibbonItemRequestDto requestDto, RibbonItem entity) {
        if (requestDto == null || entity == null) {
            return;
        }

        if (requestDto.getRibbonId() != null) {
            Ribbon ribbon = ribbonService.getRepository().findById(requestDto.getRibbonId()).orElseThrow(
                    () -> new RuntimeException("Ribbon not found")
            );
            entity.setRibbon(ribbon);
            entity.setRibbonId(requestDto.getRibbonId());
        }

        if (requestDto.getBookId() != null) {
            Book book = bookService.getRepository().findById(requestDto.getBookId()).orElseThrow(
                    () -> new RuntimeException("Book not found")
            );
            entity.setBook(book);
            entity.setBookId(requestDto.getBookId());
        }

    }
}
