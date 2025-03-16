package com.ecommerce.book_store.http.controller.api.vosk;

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
    public ResponseEntity<?> transcribeAudio(@RequestParam("audio") MultipartFile audio) {
        try {
            System.out.println("Nhận file: " + audio.getOriginalFilename() +
                    ", Kích thước: " + audio.getSize() +
                    ", Content-Type: " + audio.getContentType());

            BufferedInputStream bufferedStream = new BufferedInputStream(audio.getInputStream());

            String result = this.voskService.transcribeAudio(bufferedStream);

            Map<String, String> response = new HashMap<>();
            response.put("text", result);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();

            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
}
