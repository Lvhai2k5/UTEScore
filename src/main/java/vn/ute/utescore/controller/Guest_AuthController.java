package vn.ute.utescore.controller;

import vn.ute.utescore.dto.Guest_LoginDTO;
import vn.ute.utescore.dto.Guest_RegisterDTO;
import vn.ute.utescore.model.Roles;
import vn.ute.utescore.model.TaiKhoan;
import vn.ute.utescore.service.Guest_AuthService;
import vn.ute.utescore.utils.SessionUtil;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class Guest_AuthController {

    @Autowired
    private Guest_AuthService authService;

    // ✅ Trang đăng ký
    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("dto", new Guest_RegisterDTO());
        return "guest/register";
    }

    @PostMapping("/register")
    public String register(@ModelAttribute("dto") Guest_RegisterDTO dto, Model model) {
        try {
            authService.register(dto);
            return "redirect:/login?success";
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            return "guest/register";
        }
    }

    // ✅ Trang đăng nhập
    @GetMapping("/login")
    public String showLoginForm(Model model) {
        model.addAttribute("dto", new Guest_LoginDTO());
        return "guest/login";
    }

    @PostMapping("/login")
    public String login(@ModelAttribute("dto") Guest_LoginDTO dto, HttpSession session, Model model) {
    	try {
            TaiKhoan user = authService.login(dto);

            // ✅ Lưu session
            SessionUtil.setCustomer(session, user.getEmail(), user.getRole().getRoleName());
            System.out.println("✅ Đăng nhập thành công: " + user.getEmail() +
                    " | Role: " + user.getRole().getRoleName());

            // ✅ Chuyển hướng theo vai trò
            switch (user.getRole().getRoleName()) {
                case "KhachHang":
                    return "redirect:/customer/home";
                case "NhanVien":
                    return "redirect:/employee";
                case "QuanLy":
                    return "redirect:/admin/fields";
                default:
                    return "redirect:/login";
            }

        } catch (RuntimeException ex) {
            // ✅ Nếu có lỗi trong service thì hiển thị lại trang login
            model.addAttribute("error", ex.getMessage());
            return "guest/login"; // ❗ Không redirect, vì redirect sẽ mất attribute error
        }
    }
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }
}
