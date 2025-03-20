package com.ecommerce.book_store.http.controller.api.slider;

import com.ecommerce.book_store.http.ApiResponse;
import com.ecommerce.book_store.persistent.entity.Slider;
import com.ecommerce.book_store.service.abstraction.SliderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/sliders")
@CrossOrigin(origins = "http://localhost:5173")
public class SliderApiController {
    private final SliderService sliderService;

    public SliderApiController(SliderService sliderService) {
        this.sliderService = sliderService;
    }

    @GetMapping("/all")
    public ResponseEntity<ApiResponse<Object>> getAllSliders() {
        List<Slider> sliders = sliderService.getAllSliders();
        if (sliders.isEmpty()) {
            return ApiResponse.error("No slider found", HttpStatus.NOT_FOUND);
        }
        return ApiResponse.success(sliders, "Get all sliders successfully");

    }
}
