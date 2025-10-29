package vn.ute.utescore.service;

public interface CustomerGeminiChatService {

    /**
     * Gửi prompt đến Google Gemini và nhận phản hồi dạng text.
     *
     * @param prompt Nội dung câu hỏi hoặc hướng dẫn gửi đến Gemini
     * @return Câu trả lời text của Gemini (hoặc thông báo lỗi)
     */
    String askGemini(String prompt);
}
