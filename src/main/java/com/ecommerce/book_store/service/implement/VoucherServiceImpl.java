package com.ecommerce.book_store.service.implement;

import com.ecommerce.book_store.http.dto.request.implement.VoucherRequestDto;
import com.ecommerce.book_store.http.dto.response.implement.VoucherResponseDto;
import com.ecommerce.book_store.persistent.entity.AbstractEntity;
import com.ecommerce.book_store.persistent.entity.Voucher;
import com.ecommerce.book_store.persistent.repository.abstraction.VoucherRepository;
import com.ecommerce.book_store.service.abstraction.VoucherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VoucherServiceImpl extends IServiceImpl<VoucherRequestDto, VoucherResponseDto, Voucher>
        implements VoucherService {
    @Autowired
    public VoucherServiceImpl(VoucherRepository voucherRepository) {
        super(voucherRepository);
    }

    @Override
    public Voucher toEntity(VoucherRequestDto requestDto) {
        return null;
    }

    @Override
    public VoucherResponseDto toResponseDto(AbstractEntity entity) {
        return null;
    }

    @Override
    public void copyProperties(VoucherRequestDto requestDto, Voucher entity) {

    }
}
