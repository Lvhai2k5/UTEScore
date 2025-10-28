package vn.ute.utescore.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import vn.ute.utescore.model.ChatMessage;

import java.util.List;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {

    // Lịch sử hội thoại 2 chiều giữa A và B
    List<ChatMessage> findBySenderAndReceiverOrReceiverAndSenderOrderByTimestampAsc(
            String s1, String r1, String s2, String r2
    );

    // Danh sách khách đã/đang chat với employee + thời gian tin mới nhất (để sắp xếp)
    // Trả về dạng Object[]{otherEmail, lastTime}
    @Query("""
           SELECT 
             CASE WHEN m.sender = :employee THEN m.receiver ELSE m.sender END AS otherEmail,
             MAX(m.timestamp) AS lastTime
           FROM ChatMessage m
           WHERE m.sender = :employee OR m.receiver = :employee
           GROUP BY CASE WHEN m.sender = :employee THEN m.receiver ELSE m.sender END
           ORDER BY MAX(m.timestamp) DESC
           """)
    List<Object[]> findThreadsForEmployeeOrdered(String employee);
}
