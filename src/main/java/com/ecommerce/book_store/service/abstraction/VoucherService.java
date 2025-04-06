package com.ecommerce.book_store.service.abstraction;

import com.ecommerce.book_store.http.dto.request.implement.VoucherRequestDto;
import com.ecommerce.book_store.http.dto.response.implement.VoucherResponseDto;
import com.ecommerce.book_store.persistent.entity.Voucher;
import org.springframework.stereotype.Service;

import java.util.List;


public interface VoucherService extends IService<VoucherRequestDto, VoucherResponseDto, Voucher> {
    List<Voucher> getVoucherWithConditions(List<Long> categoryIds, Double minSpend);
    Voucher findByCode(String code);
}
