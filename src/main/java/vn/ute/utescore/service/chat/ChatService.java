package vn.ute.utescore.service.chat;

import vn.ute.utescore.dto.chat.ChatMessageDTO;
import vn.ute.utescore.model.chat.ChatConversation;
import vn.ute.utescore.model.chat.SenderType;
import java.util.*;

public interface ChatService {
    ChatConversation getOrCreateConversation(Integer khachHangId, Integer nhanVienUserID);
    List<ChatMessageDTO> getMessages(Long conversationId);
    ChatMessageDTO sendMessage(Long conversationId,
                               Integer khachHangId,
                               Integer nhanVienUserID,
                               vn.ute.utescore.model.chat.SenderType senderType,
                               Integer senderId,
                               String content);
}