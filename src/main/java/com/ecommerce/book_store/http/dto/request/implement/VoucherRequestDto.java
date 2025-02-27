package com.ecommerce.book_store.http.dto.request.implement;

import com.ecommerce.book_store.http.dto.request.AbstractRequestDto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class VoucherRequestDto extends AbstractRequestDto {
    private MultipartFile thumbnail;
    private String code;
    @Positive
    private int discount;
    @Positive
    private int minSpend;
    private LocalDateTime expiredDate;
    private List<Long> categoryIds;
}
