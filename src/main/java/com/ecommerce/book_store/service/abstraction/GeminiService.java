package com.ecommerce.book_store.service.abstraction;

import com.ecommerce.book_store.core.constant.ChatType;

public interface GeminiService {
    String generateResponse(String question, ChatType chatType);
}
