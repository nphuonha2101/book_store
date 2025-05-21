package com.ecommerce.book_store.http.controller.api.voucher;

import com.ecommerce.book_store.http.ApiResponse;
import com.ecommerce.book_store.http.dto.request.implement.VoucherApplyRequestDto;
import com.ecommerce.book_store.http.dto.request.implement.VoucherGetRequestDto;
import com.ecommerce.book_store.http.dto.response.implement.VoucherResponseDto;
import com.ecommerce.book_store.persistent.entity.Voucher;
import com.ecommerce.book_store.service.abstraction.VoucherService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/v1/auth/vouchers")
public class VoucherApiController {
    private final VoucherService voucherService;

    public VoucherApiController(VoucherService voucherService) {
        this.voucherService = voucherService;
    }

    @PostMapping("/all")
    public ResponseEntity<ApiResponse<List<VoucherResponseDto>>> getAllVouchers(@RequestBody VoucherGetRequestDto voucherGetRequestDto) {
        try {
            List<VoucherResponseDto> vouchers = voucherService.getVoucherWithConditions(voucherGetRequestDto.getCategoryIds(), voucherGetRequestDto.getMinSpend());
            return vouchers != null
                    ? ApiResponse.success(vouchers, "Voucher đã được lấy thành công")
                    : ApiResponse.error("Không tìm thấy", HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/apply")
    public ResponseEntity<ApiResponse<Long>> applyVoucher(@RequestBody VoucherApplyRequestDto voucherApplyRequestDto) {
        try {
            VoucherResponseDto voucher = voucherService.findByCode(voucherApplyRequestDto.getCode());
            if (voucher != null && voucher.getMinSpend() <= voucherApplyRequestDto.getTotalPrice() && voucher.getExpiredDate().isAfter(LocalDateTime.now())) {
                long discountPrice = Math.round(voucher.getDiscount() * voucherApplyRequestDto.getTotalPrice() / 100);
                return ApiResponse.success(discountPrice, "Voucher đã được áp dụng thành công");
            } else {
                return ApiResponse.error("Voucher không hợp lệ hoặc không thỏa mãn điều kiện", HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
