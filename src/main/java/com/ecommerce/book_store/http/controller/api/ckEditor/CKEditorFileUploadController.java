package com.ecommerce.book_store.http.controller.api.ckEditor;

import com.ecommerce.book_store.service.implement.FirebaseStorageService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
public class CKEditorFileUploadController {
    private final FirebaseStorageService firebaseStorageService;

    public CKEditorFileUploadController(FirebaseStorageService firebaseStorageService) {
        this.firebaseStorageService = firebaseStorageService;
    }

    @PostMapping("/api/v1/ck-editor/file/upload")
    public ResponseEntity<?> upload(@RequestParam("upload") MultipartFile upload) {
        try {
            String url = this.firebaseStorageService.uploadFile(upload, "ck-editor");
            return ResponseEntity.ok(Map.of("uploaded", true, "url", url));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("uploaded", false, "error", e.getMessage()));
        }

    }
}
