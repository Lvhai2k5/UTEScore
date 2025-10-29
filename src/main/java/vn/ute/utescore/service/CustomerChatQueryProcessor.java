package vn.ute.utescore.service;

import jakarta.servlet.http.HttpSession;

/**
 * Interface xử lý câu hỏi của khách hàng (Chatbot) dựa vào dữ liệu nội bộ.
 */
public interface CustomerChatQueryProcessor {

    /**
     * Xử lý câu hỏi của người dùng.
     * Nếu là câu hỏi về dữ liệu nội bộ (bảng ThueSan, SanBong, GiaThue),
     * thì trả về câu trả lời ngay. Nếu không, trả về null để chuyển sang Gemini.
     *
     * @param message  Câu hỏi khách hàng nhập
     * @param session  Session hiện tại (để lấy Khách hàng đang đăng nhập)
     * @return Câu trả lời từ DB hoặc null nếu không tìm thấy
     */
    String handleQuery(String message, HttpSession session);
}
