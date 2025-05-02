package com.ecommerce.book_store.http.controller.api.contact;

import com.ecommerce.book_store.http.ApiResponse;
import com.ecommerce.book_store.http.dto.request.implement.ContractRequestDto;
import com.ecommerce.book_store.service.abstraction.MailService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/v1/contacts")
@RestController
public class ContactUsApiController {
    private final MailService mailService;
    public ContactUsApiController(MailService mailService) {
        this.mailService = mailService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<String>> submitContactForm(@RequestBody ContractRequestDto request) {
        try {
            if (request.getEmail() == null || request.getMessage() == null) {
                return ApiResponse.error("Email and message are required", HttpStatus.BAD_REQUEST);
            }

            String subject = "[NEW] [Contact Form] Bạn có một tin nhắn mới";
            String body = "Email: " + request.getEmail() + "\n" +
                    "Message: " + request.getMessage();
            mailService.sendEmail("nphuonha2101@gmail.com", subject, body);

            return ApiResponse.success(null, "Contact form submitted successfully");
        } catch (Exception e) {
            return ApiResponse.error("Failed to submit contact form: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}