package com.ecommerce.book_store.service.implement;

import com.ecommerce.book_store.core.constant.ChatType;
import com.ecommerce.book_store.http.dto.response.implement.BookResponseDto;
import com.ecommerce.book_store.http.dto.response.implement.OrderResponseDto;
import com.ecommerce.book_store.service.abstraction.BookService;
import com.ecommerce.book_store.service.abstraction.GeminiService;
import com.ecommerce.book_store.service.abstraction.OrderService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.genai.Client;
import com.google.genai.types.GenerateContentResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Before use just set the GOOGLE_API_KEY environment variable
 * <h6>Open Terminal with Administrator Permission and type the command below: </h6>
 * <br />
 * <code>[Environment]::SetEnvironmentVariable("GOOGLE_API_KEY", "google_api_key", "Machine")</code>
 */
@Slf4j
@Service
public class GeminiServiceImpl implements GeminiService {
    private final OrderService orderService;
    private final BookService bookService;
    private final ObjectMapper objectMapper;

    public GeminiServiceImpl(OrderService orderService, BookService bookService, ObjectMapper objectMapper) {
        this.orderService = orderService;
        this.bookService = bookService;
        this.objectMapper = objectMapper;
    }

    @Override
    public String generateResponse(String question, ChatType chatType, Long userId) {
        try {
            String prompt = generatePrompt(question, chatType, userId);
            log.info("GeminiService: Generated prompt: " + prompt);
            return getGeminiResponse(prompt);
        } catch (Exception e) {
            log.error("GeminiService: Error generating response: " + e.getMessage());
            return "Đã xảy ra lỗi khi tạo phản hồi: " + e.getMessage();
        }
    }

    private String generatePrompt(String question, ChatType chatType, Long userId) throws Exception {
        StringBuilder prompt = new StringBuilder();
        prompt.append("Bạn là một trợ lý ảo thông minh của NPBookStore, chuyên cung cấp thông tin và hỗ trợ người dùng. ");
        prompt.append("Hãy trả lời câu hỏi của tôi một cách rõ ràng và chi tiết. ");
        prompt.append("Các câu hỏi có thể liên quan đến sách, đơn hàng hoặc các chủ đề khác. ");

        if (chatType == ChatType.BOOK_SEARCH) {
            log.info("GeminiService: Generating prompt for book search: " + question);
            List<BookResponseDto> books = bookService.searchRelevanceByKeyword(question, PageRequest.of(0, 5)).getContent();

            prompt.append("Tôi muốn tìm hiểu về cuốn sách có tên là '").append(question).append("'. ");
            prompt.append("Hãy cung cấp thông tin chi tiết về cuốn sách này, bao gồm tác giả, mô tả và đánh giá.");
            prompt.append("Dưới đây là kết quả của yêu cầu: ").append(objectMapper.writeValueAsString(books)).append(". ");
            prompt.append("Nếu không tìm thấy sách nào, hãy thông báo cho tôi biết.");
        } else if (chatType == ChatType.ORDER_LOOKUP) {
            String idPattern = "\\b\\d+\\b";
            java.util.regex.Pattern pattern = java.util.regex.Pattern.compile(idPattern);
            java.util.regex.Matcher matcher = pattern.matcher(question);

            OrderResponseDto order = null;

            if (matcher.find()) {
                String extractedId = matcher.group();
                order = orderService.findByIdAndUserId(Long.parseLong(extractedId), userId);
            }

            prompt.append("Tôi có câu hỏi: '").append(question).append("'. ");
            prompt.append("Hãy cung cấp thông tin về đơn hàng của tôi nếu có. ");
            if (order != null) {
                prompt.append("Dưới đây là thông tin đơn hàng của tôi: ").append(objectMapper.writeValueAsString(order)).append(". ");
            } else {
                prompt.append("Nếu không có thông tin nào liên quan, hãy trả lời rằng không có thông tin.");
            }
        } else if (chatType == ChatType.GENERAL) {
            prompt.append("Tôi có câu hỏi: '").append(question).append("'. ");
            prompt.append("Hãy trả lời câu hỏi này một cách ngắn gọn và súc tích.");
        } else {
            throw new IllegalArgumentException("Unknown chat type: " + chatType);
        }

        return prompt.toString();
    }

    private String getGeminiResponse(String prompt) {
        try (Client client = new Client()) {
            log.info("GeminiService: Sending prompt to Gemini: " + prompt);

            GenerateContentResponse response = client.models.generateContent(
                    "gemini-2.0-flash",
                    prompt,
                    null
            );
            return response.text();
        } catch (Exception e) {
            log.error("GeminiService: " + e.getMessage());
            return "Đã xảy ra lỗi khi tạo phản hồi từ Gemini: " + e.getMessage();
        }
    }
}
