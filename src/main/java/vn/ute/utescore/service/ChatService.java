package vn.ute.utescore.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.ute.utescore.model.ChatMessage;
import vn.ute.utescore.repository.ChatMessageRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class ChatService {

    private final ChatMessageRepository chatRepo;

    public ChatService(ChatMessageRepository chatRepo) {
        this.chatRepo = chatRepo;
    }

    public ChatMessage save(String sender, String receiver, String content) {
        ChatMessage msg = new ChatMessage(sender, receiver, content, LocalDateTime.now());
        return chatRepo.save(msg);
    }

    public List<ChatMessage> getHistory(String sender, String receiver) {
        return chatRepo.findBySenderAndReceiverOrReceiverAndSenderOrderByTimestampAsc(
                sender, receiver, sender, receiver
        );
    }
}
