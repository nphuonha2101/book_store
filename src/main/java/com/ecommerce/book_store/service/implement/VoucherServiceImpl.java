package com.ecommerce.book_store.service.implement;

import com.ecommerce.book_store.http.dto.request.implement.VoucherRequestDto;
import com.ecommerce.book_store.http.dto.response.implement.VoucherResponseDto;
import com.ecommerce.book_store.persistent.entity.AbstractEntity;
import com.ecommerce.book_store.persistent.entity.Category;
import com.ecommerce.book_store.persistent.entity.Voucher;
import com.ecommerce.book_store.persistent.repository.abstraction.VoucherRepository;
import com.ecommerce.book_store.service.abstraction.CategoryService;
import com.ecommerce.book_store.service.abstraction.VoucherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class VoucherServiceImpl extends IServiceImpl<VoucherRequestDto, VoucherResponseDto, Voucher>
        implements VoucherService {
    private final FirebaseStorageService firebaseStorageService;
    private final CategoryService categoryService;
    @Autowired
    public VoucherServiceImpl(VoucherRepository voucherRepository, FirebaseStorageService firebaseStorageService, CategoryService categoryService) {
        super(voucherRepository);
        this.firebaseStorageService = firebaseStorageService;
        this.categoryService = categoryService;
    }

    @Override
    public Voucher toEntity(VoucherRequestDto requestDto) {
        Voucher voucher = new Voucher();
        if (requestDto.getThumbnail() != null) {
            String thumbnail = firebaseStorageService.uploadFile(requestDto.getThumbnail(), "vouchers/thumbnails");
            voucher.setThumbnail(thumbnail);
        }
        List<Category> categories = new ArrayList<>();
        for (Long categoryId : requestDto.getCategoryIds()) {
            Category category = new Category(categoryId);
            categories.add(category);
        }
        voucher.setCategories(categories);

        voucher.setCode(requestDto.getCode());
        voucher.setDiscount(requestDto.getDiscount());
        voucher.setMinSpend(requestDto.getMinSpend());
        voucher.setExpiredDate(requestDto.getExpiredDate());

        return voucher;
    }

    @Override
    public VoucherResponseDto toResponseDto(AbstractEntity entity) {
        if (entity == null) {
            return null;
        }
        return new VoucherResponseDto(
                entity.getId(),
                ((Voucher) entity).getThumbnail(),
                ((Voucher) entity).getCode(),
                ((Voucher) entity).getDiscount(),
                ((Voucher) entity).getMinSpend(),
                ((Voucher) entity).getExpiredDate(),
                ((Voucher) entity).getCategories().stream()
                        .map(categoryService::toResponseDto)
                        .toList()
        );
    }

    @Override
    public void copyProperties(VoucherRequestDto requestDto, Voucher entity) {
        if (requestDto.getThumbnail() != null && !requestDto.getThumbnail().isEmpty()) {
            String thumbnail = firebaseStorageService.uploadFile(requestDto.getThumbnail(), "vouchers/thumbnails");
            entity.setThumbnail(thumbnail);
        }
        if (requestDto.getCode() != null) {
            entity.setCode(requestDto.getCode());
        }
        if (requestDto.getDiscount() != 0) {
            entity.setDiscount(requestDto.getDiscount());
        }
        if (requestDto.getMinSpend() != 0) {
            entity.setMinSpend(requestDto.getMinSpend());
        }
        if (requestDto.getExpiredDate() != null) {
            entity.setExpiredDate(requestDto.getExpiredDate());
        }

        if (requestDto.getCategoryIds() != null) {
            List<Category> categories = new ArrayList<>();
            for (Long categoryId : requestDto.getCategoryIds()) {
                Category category = new Category(categoryId);
                categories.add(category);
            }
            entity.getCategories().clear();
            entity.setCategories(categories);
        }
    }

    @Override
    public List<VoucherResponseDto> getVoucherWithConditions(List<Long> categoryIds, Double minSpend) {
        return ((VoucherRepository) repository).getVoucherWithConditions(categoryIds, minSpend)
                .stream()
                .map(this::toResponseDto)
                .toList();

    }

    @Override
    public VoucherResponseDto findByCode(String code) {
        Voucher voucher = ((VoucherRepository) repository).findByCode(code);
        if (voucher == null) {
            return null;
        }
        return toResponseDto(voucher);
    }
}
