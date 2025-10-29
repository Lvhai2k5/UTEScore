package vn.ute.utescore.dto;

/**
 * Dữ liệu request khi khách hàng gửi tin nhắn chat.
 */
public class CustomerChatRequest {
    private String message;

    // ✅ Constructor rỗng (bắt buộc cho JSON mapping)
    public CustomerChatRequest() {}

    // ✅ Constructor đầy đủ
    public CustomerChatRequest(String message) {
        this.message = message;
    }

    // ✅ Getter và Setter (bắt buộc để Spring đọc JSON)
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
