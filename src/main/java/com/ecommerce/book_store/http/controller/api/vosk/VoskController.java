package com.ecommerce.book_store.http.controller.api.vosk;

import com.ecommerce.book_store.http.ApiResponse;
import com.ecommerce.book_store.service.abstraction.VoskService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedInputStream;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/vosk")
public class VoskController {
    private final VoskService voskService;

    public VoskController(VoskService voskService) {
        this.voskService = voskService;
    }

    @PostMapping("/transcribe")
    public ResponseEntity<ApiResponse<Object>> transcribeAudio(@RequestParam("audio") MultipartFile audio) {
        try {
            System.out.println("Nhận file: " + audio.getOriginalFilename() +
                    ", Kích thước: " + audio.getSize() +
                    ", Content-Type: " + audio.getContentType());

            BufferedInputStream bufferedStream = new BufferedInputStream(audio.getInputStream());

            String result = this.voskService.transcribeAudio(bufferedStream);
            return ApiResponse.success(result, "Kết quả được trả về thành công");
        } catch (Exception e) {
            e.printStackTrace();
            return ApiResponse.error(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
