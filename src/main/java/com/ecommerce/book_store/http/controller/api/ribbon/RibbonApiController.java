package com.ecommerce.book_store.http.controller.api.ribbon;

import com.ecommerce.book_store.http.ApiResponse;
import com.ecommerce.book_store.http.dto.response.implement.RibbonResponseDto;
import com.ecommerce.book_store.service.abstraction.RibbonService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/ribbons")
public class RibbonApiController {
    private final RibbonService ribbonService;

    public RibbonApiController(RibbonService ribbonService) {
        this.ribbonService = ribbonService;
    }

    @GetMapping("/all")
    public ResponseEntity<ApiResponse<List<RibbonResponseDto>>> getAllRibbons() {
        try {
            List<RibbonResponseDto> ribbons = ribbonService.findAllByStatusTrue();
            return ribbons != null ? ApiResponse.success(ribbons, "Tất cả ribbon được lấy thành công") : ApiResponse.error("Không tìm thấy", HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
