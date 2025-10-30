package vn.ute.utescore.service.chat.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import vn.ute.utescore.dto.chat.ChatMessageDTO;
import vn.ute.utescore.model.KhachHang;
import vn.ute.utescore.model.NhanVien;
import vn.ute.utescore.model.chat.*;
import vn.ute.utescore.repository.KhachHangRepository;
import vn.ute.utescore.repository.NhanVienRepository;
import vn.ute.utescore.repository.chat.ChatConversationRepository;
import vn.ute.utescore.repository.chat.ChatMessageRepository;
import vn.ute.utescore.service.chat.ChatService;

@Service
public class ChatServiceImpl implements ChatService {

    @Autowired private ChatConversationRepository conversationRepo;
    @Autowired private ChatMessageRepository messageRepo;
    @Autowired private KhachHangRepository khachHangRepo;
    @Autowired private NhanVienRepository nhanVienRepo;
    @Autowired private SimpMessagingTemplate messagingTemplate;

    @Override
    @Transactional
    public ChatConversation getOrCreateConversation(Integer khachHangId, Integer nhanVienUserID) {
        // ✅ Chặn lỗi null/0 để tránh NoSuchElementException
        if (khachHangId == null || khachHangId <= 0)
            throw new RuntimeException("ID Khách hàng không hợp lệ: " + khachHangId);
        if (nhanVienUserID == null || nhanVienUserID <= 0)
            throw new RuntimeException("ID Nhân viên không hợp lệ: " + nhanVienUserID);

        Optional<ChatConversation> opt = conversationRepo.findByKHAndNV(khachHangId, nhanVienUserID);
        if (opt.isPresent()) return opt.get();

        KhachHang kh = khachHangRepo.findById(khachHangId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy Khách hàng ID = " + khachHangId));
        NhanVien nv = nhanVienRepo.findById(nhanVienUserID)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy Nhân viên ID = " + nhanVienUserID));
        ChatConversation c = new ChatConversation();
        c.setKhachHang(kh);
        c.setNhanVien(nv);
        c.setCreatedAt(LocalDateTime.now());
        c.setLastMessageAt(LocalDateTime.now());
        return conversationRepo.save(c);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ChatMessageDTO> getMessages(Long conversationId) {
        return messageRepo.findByConversation(conversationId).stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ChatMessageDTO sendMessage(Long conversationId,
                                      Integer khachHangId,
                                      Integer nhanVienUserID,
                                      SenderType senderType,
                                      Integer senderId,
                                      String content) {
        ChatConversation c = (conversationId != null)
                ? conversationRepo.findById(conversationId).orElseThrow()
                : getOrCreateConversation(khachHangId, nhanVienUserID);

        ChatMessage m = new ChatMessage();
        m.setConversation(c);
        m.setSenderType(senderType);
        m.setSenderId(senderId);
        m.setContent(content);
        m.setCreatedAt(LocalDateTime.now());
        messageRepo.save(m);

        c.setLastMessageAt(LocalDateTime.now());
        conversationRepo.save(c);

        ChatMessageDTO dto = toDTO(m);
        messagingTemplate.convertAndSend("/topic/chat/" + c.getId(), dto);
        return dto;
    }

    private ChatMessageDTO toDTO(ChatMessage m) {
        ChatMessageDTO dto = new ChatMessageDTO();
        dto.setConversationId(m.getConversation().getId());
        dto.setMessageId(m.getId());
        dto.setSenderType(m.getSenderType());
        dto.setSenderId(m.getSenderId());
        dto.setContent(m.getContent());
        dto.setCreatedAt(m.getCreatedAt());
        return dto;
    }
}