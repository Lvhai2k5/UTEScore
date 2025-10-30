package vn.ute.utescore.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import vn.ute.utescore.model.NhanVien;
import vn.ute.utescore.repository.NhanVienRepository;
import vn.ute.utescore.utils.SessionUtil;

import java.util.Optional;

@Controller
public class EmployeeProfileController {

    private final NhanVienRepository nhanVienRepository;

    public EmployeeProfileController(NhanVienRepository nhanVienRepository) {
        this.nhanVienRepository = nhanVienRepository;
    }

    // ✅ Hiển thị trang thông tin cá nhân của nhân viên đang đăng nhập
    @GetMapping("/employee/profile")
    public String profile(Model model, HttpSession session) {

        // 📨 Lấy email nhân viên đang đăng nhập từ session
        String email = SessionUtil.getCustomerEmail(session);

        if (email != null) {
            // 🔍 Tìm nhân viên theo email
            Optional<NhanVien> optionalNV = nhanVienRepository.findByEmail(email);

            if (optionalNV.isPresent()) {
                NhanVien nhanVien = optionalNV.get();
                model.addAttribute("nhanVienDangNhap", nhanVien); // Sidebar
                model.addAttribute("nhanVien", nhanVien);         // Nội dung chính
            } else {
                model.addAttribute("msg", "Không tìm thấy thông tin nhân viên!");
            }
        } else {
            // ❌ Nếu chưa đăng nhập → quay lại login
            return "redirect:/login";
        }

        model.addAttribute("pageTitle", "UTEScore – Thông tin nhân viên");
        return "employee/profile";
    }
}
