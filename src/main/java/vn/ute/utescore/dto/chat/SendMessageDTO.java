package vn.ute.utescore.dto.chat;

public class SendMessageDTO {
    private Long conversationId; // nếu null → tạo theo khachHangId + nhanVienId
    private Integer khachHangId;
    private Integer nhanVienId;
    private String content;

    public Long getConversationId() { return conversationId; }
    public void setConversationId(Long conversationId) { this.conversationId = conversationId; }
    public Integer getKhachHangId() { return khachHangId; }
    public void setKhachHangId(Integer khachHangId) { this.khachHangId = khachHangId; }
    public Integer getNhanVienId() { return nhanVienId; }
    public void setNhanVienId(Integer nhanVienId) { this.nhanVienId = nhanVienId; }
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
}