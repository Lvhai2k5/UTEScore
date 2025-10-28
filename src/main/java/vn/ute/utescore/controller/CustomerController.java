package vn.ute.utescore.controller;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import vn.ute.utescore.model.KhachHang;
import vn.ute.utescore.model.TaiKhoan;
import vn.ute.utescore.repository.CustomerKhachHangRepository;
import vn.ute.utescore.repository.CustomerTaiKhoanRepository;
import vn.ute.utescore.utils.SessionUtil;

import java.io.IOException;
import java.util.Base64;

@Controller
@RequestMapping("/customer")
public class CustomerController {

	private final CustomerKhachHangRepository khachHangRepository;
    private final CustomerTaiKhoanRepository taiKhoanRepository;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public CustomerController(CustomerKhachHangRepository khachHangRepository,
                              CustomerTaiKhoanRepository taiKhoanRepository) {
        this.khachHangRepository = khachHangRepository;
        this.taiKhoanRepository = taiKhoanRepository;
    }

    // 🏠 Trang chủ khách hàng
    @GetMapping("/home")
    public String home(Model model, HttpSession session) {
        String email = SessionUtil.getCustomerEmail(session);
        if (email == null) return "redirect:/login";

        KhachHang kh = khachHangRepository.findByEmail(email).orElse(null);
        if (kh == null) return "redirect:/login";
        if (kh.getAnhDaiDien() != null) {
            model.addAttribute("anhBase64",
                    Base64.getEncoder().encodeToString(kh.getAnhDaiDien()));
        }
        model.addAttribute("khachHang", kh);
        model.addAttribute("pageTitle", "Trang chủ khách hàng");
        return "customer/home";
    }
    
    // 👤 Trang tài khoản cá nhân
    @GetMapping("/account")
    public String account(Model model, HttpSession session) {
        String email = SessionUtil.getCustomerEmail(session);
        if (email == null) return "redirect:/login";

        KhachHang kh = khachHangRepository.findByEmail(email).orElse(null);
        if (kh == null) return "redirect:/login";

        if (kh.getAnhDaiDien() != null) {
            model.addAttribute("anhBase64",
                    Base64.getEncoder().encodeToString(kh.getAnhDaiDien()));
        }

        model.addAttribute("khachHang", kh);
        model.addAttribute("pageTitle", "Tài khoản cá nhân");
        return "customer/account";
    }

    // 💾 Cập nhật thông tin cá nhân
    @PostMapping("/account/update")
    public String updateAccount(@RequestParam("HoTen") String hoTen,
                                @RequestParam("SoDienThoai") String soDienThoai,
                                @RequestParam(value = "anhDaiDienFile", required = false) MultipartFile file,
                                HttpSession session, Model model) {

        String email = SessionUtil.getCustomerEmail(session);
        if (email == null) return "redirect:/login";

        KhachHang kh = khachHangRepository.findByEmail(email).orElse(null);
        if (kh == null) return "redirect:/login";

        try {
            kh.setHoTen(hoTen);
            kh.setSoDienThoai(soDienThoai);

            if (file != null && !file.isEmpty()) {
                kh.setAnhDaiDien(file.getBytes());
            }

            khachHangRepository.save(kh);
            model.addAttribute("successMessage", "✅ Cập nhật thông tin thành công!");
        } catch (IOException e) {
            model.addAttribute("errorMessage", "⚠️ Lỗi khi tải ảnh: " + e.getMessage());
        }

        if (kh.getAnhDaiDien() != null)
            model.addAttribute("anhBase64",
                    Base64.getEncoder().encodeToString(kh.getAnhDaiDien()));

        model.addAttribute("khachHang", kh);
        model.addAttribute("pageTitle", "Tài khoản cá nhân");
        return "customer/account";
    }

    // 🔒 Đổi mật khẩu
    @PostMapping("/account/change-password")
    public String changePassword(@RequestParam("oldPassword") String oldPassword,
                                 @RequestParam("newPassword") String newPassword,
                                 @RequestParam("confirmPassword") String confirmPassword,
                                 HttpSession session, Model model) {

        String email = SessionUtil.getCustomerEmail(session);
        if (email == null) return "redirect:/login";

        TaiKhoan tk = taiKhoanRepository.findById(email).orElse(null);
        if (tk == null) {
            model.addAttribute("errorMessage", "❌ Không tìm thấy tài khoản!");
        } else if (!passwordEncoder.matches(oldPassword, tk.getMatKhau())) {
            model.addAttribute("errorMessage", "❌ Mật khẩu cũ không chính xác!");
        } else if (!newPassword.equals(confirmPassword)) {
            model.addAttribute("errorMessage", "⚠️ Mật khẩu mới và xác nhận không trùng khớp!");
        } else {
            tk.setMatKhau(passwordEncoder.encode(newPassword));
            taiKhoanRepository.save(tk);
            model.addAttribute("successMessage", "🔑 Đổi mật khẩu thành công!");
        }

        KhachHang kh = khachHangRepository.findByEmail(email).orElse(null);
        if (kh != null && kh.getAnhDaiDien() != null)
            model.addAttribute("anhBase64",
                    Base64.getEncoder().encodeToString(kh.getAnhDaiDien()));

        model.addAttribute("khachHang", kh);
        model.addAttribute("pageTitle", "Tài khoản cá nhân");
        return "customer/account";
    }
}
