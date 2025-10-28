package vn.ute.utescore.controller;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import vn.ute.utescore.model.ChatMessage;
import vn.ute.utescore.repository.ChatMessageRepository;

import java.time.LocalDateTime;

@Controller
public class ChatSocketController {

    private final SimpMessagingTemplate messagingTemplate;
    private final ChatMessageRepository chatRepo;

    public ChatSocketController(SimpMessagingTemplate messagingTemplate,
                                ChatMessageRepository chatRepo) {
        this.messagingTemplate = messagingTemplate;
        this.chatRepo = chatRepo;
    }

    // Client gửi: /app/chat.send  (payload: {sender, receiver, content})
    @MessageMapping("/chat.send")
    public void send(@Payload ChatMessage msg) {
        msg.setTimestamp(LocalDateTime.now());
        chatRepo.save(msg);

        // đẩy cho người nhận (hộp thư của receiver)
        messagingTemplate.convertAndSend("/topic/chat/" + msg.getReceiver(), msg);
        // và đẩy cho chính người gửi để đồng bộ (nếu đang mở đa tab)
        messagingTemplate.convertAndSend("/topic/chat/" + msg.getSender(), msg);
    }
}
