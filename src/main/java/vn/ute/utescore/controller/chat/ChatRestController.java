package vn.ute.utescore.controller.chat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.ute.utescore.dto.chat.ChatMessageDTO;
import vn.ute.utescore.model.chat.ChatConversation;
import vn.ute.utescore.service.chat.ChatService;
import java.util.List;

@RestController
@RequestMapping("/api/chat")
public class ChatRestController {
    @Autowired private ChatService chatService;

    @GetMapping("/{conversationId}/messages")
    public ResponseEntity<List<ChatMessageDTO>> getMessages(@PathVariable Long conversationId) {
        return ResponseEntity.ok(chatService.getMessages(conversationId));
    }

    @GetMapping("/conversation")
    public ResponseEntity<Long> getOrCreate(@RequestParam Integer khachHangId, @RequestParam Integer nhanVienId) {
        ChatConversation c = chatService.getOrCreateConversation(khachHangId, nhanVienId);
        return ResponseEntity.ok(c.getId());
    }
}
