package vn.ute.utescore.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import vn.ute.utescore.model.NhanVien;
import vn.ute.utescore.repository.NhanVienRepository;

import java.util.Optional;

@Controller
public class EmployeeProfileController {

    private final NhanVienRepository nhanVienRepository;

    public EmployeeProfileController(NhanVienRepository nhanVienRepository) {
        this.nhanVienRepository = nhanVienRepository;
    }

    // ✅ Hiển thị trang thông tin cá nhân
    @GetMapping("/employee/profile")
    public String profile(Model model) {
        // Ví dụ: đang đăng nhập nhân viên có ID = 1
        // (Sau này bạn có thể lấy từ session hoặc SecurityContext)
        int userId = 1;

        Optional<NhanVien> nv = nhanVienRepository.findById(userId);
        if (nv.isPresent()) {
            model.addAttribute("nhanVien", nv.get());
        } else {
            model.addAttribute("msg", "Không tìm thấy thông tin nhân viên!");
        }

        return "employee/profile"; // file HTML bạn gửi
    }
}
