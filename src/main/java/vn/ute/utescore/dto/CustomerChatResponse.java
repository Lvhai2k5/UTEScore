package vn.ute.utescore.dto;

/**
 * Dữ liệu phản hồi của chatbot gửi về client.
 */
public class CustomerChatResponse {

    private String answer;

    public CustomerChatResponse() {
    }

    public CustomerChatResponse(String answer) {
        this.answer = answer;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }
}
