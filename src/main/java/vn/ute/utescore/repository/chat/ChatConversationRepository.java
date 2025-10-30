package vn.ute.utescore.repository.chat;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import vn.ute.utescore.model.chat.ChatConversation;
import java.util.*;

public interface ChatConversationRepository extends JpaRepository<ChatConversation, Long> {
    @Query("SELECT c FROM ChatConversation c WHERE c.khachHang.maKhachHang = :kh AND c.nhanVien.userID = :nv")
    Optional<ChatConversation> findByKHAndNV(@Param("kh") Integer maKhachHang, @Param("nv") Integer userID);

    @Query("SELECT c FROM ChatConversation c WHERE c.nhanVien.userID = :nv ORDER BY c.lastMessageAt DESC")
    List<ChatConversation> findAllByNhanVien(@Param("nv") Integer userID);

    @Query("SELECT c FROM ChatConversation c WHERE c.khachHang.maKhachHang = :kh ORDER BY c.lastMessageAt DESC")
    List<ChatConversation> findAllByKhachHang(@Param("kh") Integer maKhachHang);
}