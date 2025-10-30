package vn.ute.utescore.controller.chat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import vn.ute.utescore.dto.chat.SendMessageDTO;
import vn.ute.utescore.model.chat.SenderType;
import vn.ute.utescore.service.chat.ChatService;

@Controller
public class ChatWsController {
    @Autowired private ChatService chatService;

    @MessageMapping("/chat.send")
    public void send(SendMessageDTO payload) {
        SenderType st = (payload.getNhanVienId() != null && payload.getKhachHangId() == null)
                ? SenderType.NHAN_VIEN
                : SenderType.KHACH_HANG;
        Integer senderId = (st == SenderType.NHAN_VIEN) ? payload.getNhanVienId() : payload.getKhachHangId();
        chatService.sendMessage(
                payload.getConversationId(),
                payload.getKhachHangId(),
                payload.getNhanVienId(),
                st,
                senderId,
                payload.getContent()
        );
    }
}