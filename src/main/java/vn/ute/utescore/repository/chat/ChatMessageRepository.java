package vn.ute.utescore.repository.chat;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import vn.ute.utescore.model.chat.ChatMessage;
import java.util.List;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
    @Query("SELECT m FROM ChatMessage m WHERE m.conversation.id = :cid ORDER BY m.createdAt ASC")
    List<ChatMessage> findByConversation(@Param("cid") Long conversationId);
}