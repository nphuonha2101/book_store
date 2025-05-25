package com.ecommerce.book_store.http.controller.api.chat;

import com.ecommerce.book_store.core.constant.ChatType;
import com.ecommerce.book_store.http.ApiResponse;
import com.ecommerce.book_store.http.dto.request.implement.MessageRequestDto;
import com.ecommerce.book_store.service.abstraction.BookService;
import com.ecommerce.book_store.service.abstraction.GeminiService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/chat")
public class ChatApiController {
    private final BookService bookService;
    private final GeminiService geminiService;

    public ChatApiController(BookService bookService, GeminiService geminiService) {
        this.bookService = bookService;
        this.geminiService = geminiService;
    }

    @PostMapping("/send")
    public ResponseEntity<ApiResponse<String>> sendChat(@RequestBody MessageRequestDto messageRequestDto) {
        try {
            ChatType chatType = ChatType.fromString(messageRequestDto.getChatType());
            String message = messageRequestDto.getMessage();

            String response = geminiService.generateResponse(message, chatType);

            return ApiResponse.success(response, "Chat successfully");
        } catch (Exception e) {
            return ApiResponse.error("Failed to process chat message: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
