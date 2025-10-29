package vn.ute.utescore.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.ute.utescore.dto.CustomerChatRequest;
import vn.ute.utescore.dto.CustomerChatResponse;
import vn.ute.utescore.service.CustomerGeminiChatService;
import vn.ute.utescore.service.CustomerChatQueryProcessor;
import vn.ute.utescore.utils.CustomerChatPolicy;

@RestController
@RequestMapping("/customer/chat")
public class CustomerChatController {

    private final CustomerGeminiChatService geminiService;
    private final CustomerChatQueryProcessor queryProcessor;

    // ✅ Constructor không dùng Lombok
    public CustomerChatController(CustomerGeminiChatService geminiService,
                                  CustomerChatQueryProcessor queryProcessor) {
        this.geminiService = geminiService;
        this.queryProcessor = queryProcessor;
    }

    // ✅ Xử lý POST request từ client chatbot
    @PostMapping
    public ResponseEntity<CustomerChatResponse> chat(
            @RequestBody CustomerChatRequest chatRequest,
            HttpSession session) {

        // ⚙️ Lấy nội dung người dùng nhập
        String message = chatRequest.getMessage();

        // 🔒 1. Kiểm tra nội dung nhạy cảm
        if (CustomerChatPolicy.isSensitive(message)) {
            return ResponseEntity.ok(
                    new CustomerChatResponse("⚠️ Xin lỗi, tôi không thể cung cấp thông tin cá nhân hoặc nhạy cảm.")
            );
        }

        // 🧠 2. Xử lý câu hỏi liên quan đến CSDL
        String dbAnswer = queryProcessor.handleQuery(message, session);
        if (dbAnswer != null) {
            return ResponseEntity.ok(new CustomerChatResponse(dbAnswer));
        }

        // 🤖 3. Gọi Gemini để trả lời tự nhiên
        String context = """
                Bạn là chatbot hỗ trợ khách thuê sân bóng UTEScore.
                Trả lời thân thiện, ngắn gọn và không tiết lộ dữ liệu cá nhân.
                """;

        String answer = geminiService.askGemini(context + "\n\nCâu hỏi: " + message);
        return ResponseEntity.ok(new CustomerChatResponse(answer));
    }
}
