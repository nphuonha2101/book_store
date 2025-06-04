package com.ecommerce.book_store.http.controller.api.vosk;

import com.ecommerce.book_store.http.ApiResponse;
import com.ecommerce.book_store.service.abstraction.SpeechToTextService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedInputStream;

@Slf4j
@RestController
@RequestMapping("/api/v1/stt")
public class SpeechToTextApiController {
    private final SpeechToTextService speechToTextService;

    public SpeechToTextApiController(SpeechToTextService speechToTextService) {
        this.speechToTextService = speechToTextService;
    }

    @PostMapping("/transcribe")
    public ResponseEntity<ApiResponse<Object>> transcribeAudio(@RequestParam("audio") MultipartFile audio) {
        try {
            System.out.println("Nhận file: " + audio.getOriginalFilename() +
                    ", Kích thước: " + audio.getSize() +
                    ", Content-Type: " + audio.getContentType());

            BufferedInputStream bufferedStream = new BufferedInputStream(audio.getInputStream());

            String result = this.speechToTextService.transcribeAudio(bufferedStream);
            return ApiResponse.success(result, "Kết quả được trả về thành công");
        } catch (Exception e) {
            log.error("Failed to transcribe audio: {}", e.getMessage());
            return ApiResponse.error("Có lỗi xảy ra", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
