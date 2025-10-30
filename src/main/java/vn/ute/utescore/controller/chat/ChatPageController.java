package vn.ute.utescore.controller.chat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpSession;
import java.util.*;

import vn.ute.utescore.model.NhanVien;
import vn.ute.utescore.model.KhachHang;
import vn.ute.utescore.repository.NhanVienRepository;
import vn.ute.utescore.repository.KhachHangRepository;
import vn.ute.utescore.repository.chat.ChatConversationRepository;
import vn.ute.utescore.utils.SessionUtil;

@Controller
@RequestMapping("/chat")
public class ChatPageController {
    @Autowired private NhanVienRepository nhanVienRepo;
    @Autowired private KhachHangRepository khachHangRepo;
    @Autowired private ChatConversationRepository conversationRepo;

    @GetMapping("/employee")
    public String employeeChat(HttpSession session, Model model) {
        String email = SessionUtil.getCustomerEmail(session); // Đổi sang getEmployeeEmail nếu bạn có.
        if (email == null) return "redirect:/login";
        Optional<NhanVien> nvOpt = nhanVienRepo.findByEmail(email);
        if (nvOpt.isEmpty()) return "redirect:/login";
        NhanVien nv = nvOpt.get();
        model.addAttribute("nhanVienDangNhap", nv);
        model.addAttribute("conversations", conversationRepo.findAllByNhanVien(nv.getUserID()));
        return "employee/chat";
    }

    @GetMapping("/customer")
    public String customerChat(HttpSession session, Model model) {
        String email = SessionUtil.getCustomerEmail(session);
        if (email == null) return "redirect:/login";
        Optional<KhachHang> khOpt = khachHangRepo.findByEmail(email);
        if (khOpt.isEmpty()) return "redirect:/login";
        KhachHang kh = khOpt.get();
        model.addAttribute("khachHangDangNhap", kh);
        model.addAttribute("nhanViens", nhanVienRepo.findAll());
        model.addAttribute("conversations", conversationRepo.findAllByKhachHang(kh.getMaKhachHang()));
        return "customer/chat";
    }
}