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
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "guest/index"; // về trang index khách
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

        // 🧠 Regex kiểm tra họ tên tiếng Việt: chỉ cho phép chữ, dấu, khoảng trắng
        String hoTenPattern = "^[A-Za-zÀ-Ỹà-ỹĂăÂâÊêÔôƠơƯưĐđ\\s]{2,50}$";

        // 🧩 Kiểm tra dữ liệu đầu vào
        if (hoTen == null || hoTen.trim().isEmpty()) {
            model.addAttribute("errorMessage", "⚠️ Họ tên không được để trống!");
        } else if (!hoTen.trim().matches(hoTenPattern)) {
            model.addAttribute("errorMessage", "⚠️ Họ tên không hợp lệ! (Chỉ chứa chữ cái, có thể có dấu và khoảng trắng)");
        } else if (soDienThoai == null || soDienThoai.trim().isEmpty()) {
            model.addAttribute("errorMessage", "⚠️ Số điện thoại không được để trống!");
        } else if (!soDienThoai.matches("^0\\d{9,10}$")) {
            model.addAttribute("errorMessage", "⚠️ Số điện thoại không hợp lệ! (Phải bắt đầu bằng 0 và có 10–11 số)");
        } else {
            try {
                kh.setHoTen(hoTen.trim());
                kh.setSoDienThoai(soDienThoai.trim());

                // 🖼️ Kiểm tra file ảnh (nếu có)
                if (file != null && !file.isEmpty()) {
                    String contentType = file.getContentType();
                    if (contentType == null || 
                        !(contentType.equals("image/jpeg") || contentType.equals("image/png"))) {
                        model.addAttribute("errorMessage", "⚠️ Ảnh đại diện chỉ chấp nhận định dạng JPG hoặc PNG!");
                    } else {
                        kh.setAnhDaiDien(file.getBytes());
                        model.addAttribute("successMessage", "✅ Cập nhật thông tin và ảnh đại diện thành công!");
                    }
                } else {
                    model.addAttribute("successMessage", "✅ Cập nhật thông tin thành công!");
                }

                khachHangRepository.save(kh);

            } catch (IOException e) {
                model.addAttribute("errorMessage", "⚠️ Lỗi khi tải ảnh: " + e.getMessage());
            }
        }

        // 📸 Gửi lại ảnh để hiển thị
        if (kh.getAnhDaiDien() != null) {
            model.addAttribute("anhBase64",
                    Base64.getEncoder().encodeToString(kh.getAnhDaiDien()));
        }

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
