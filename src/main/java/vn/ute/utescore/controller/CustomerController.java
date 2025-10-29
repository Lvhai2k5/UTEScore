package vn.ute.utescore.controller;

import jakarta.servlet.http.HttpSession;
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
import java.util.HashMap;
import java.util.Map;

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
            model.addAttribute("anhBase64", Base64.getEncoder().encodeToString(kh.getAnhDaiDien()));
        }

        model.addAttribute("khachHang", kh);
        model.addAttribute("pageTitle", "Trang chủ khách hàng");
        return "customer/home";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "guest/index";
    }

    // 👤 Trang tài khoản cá nhân
    @GetMapping("/account")
    public String account(Model model, HttpSession session) {
        String email = SessionUtil.getCustomerEmail(session);
        if (email == null) return "redirect:/login";

        KhachHang kh = khachHangRepository.findByEmail(email).orElse(null);
        if (kh == null) return "redirect:/login";

        if (kh.getAnhDaiDien() != null) {
            model.addAttribute("anhBase64", Base64.getEncoder().encodeToString(kh.getAnhDaiDien()));
        }

        model.addAttribute("khachHang", kh);
        model.addAttribute("pageTitle", "Tài khoản cá nhân");
        return "customer/account";
    }

    // 💾 Cập nhật thông tin cá nhân (form thường)
    @PostMapping("/account/update")
    public String updateAccount(@RequestParam("HoTen") String hoTen,
                                @RequestParam("SoDienThoai") String soDienThoai,
                                @RequestParam(value = "anhDaiDienFile", required = false) MultipartFile file,
                                HttpSession session, Model model) {

        String email = SessionUtil.getCustomerEmail(session);
        if (email == null) return "redirect:/login";

        KhachHang kh = khachHangRepository.findByEmail(email).orElse(null);
        if (kh == null) return "redirect:/login";

        String hoTenPattern = "^[A-Za-zÀ-Ỹà-ỹĂăÂâÊêÔôƠơƯưĐđ\\s]{2,50}$";

        if (hoTen == null || hoTen.trim().isEmpty()) {
            model.addAttribute("errorMessage", "⚠️ Họ tên không được để trống!");
        } else if (!hoTen.trim().matches(hoTenPattern)) {
            model.addAttribute("errorMessage", "⚠️ Họ tên không hợp lệ (chỉ chứa chữ cái và khoảng trắng)!");
        } else if (soDienThoai == null || soDienThoai.trim().isEmpty()) {
            model.addAttribute("errorMessage", "⚠️ Số điện thoại không được để trống!");
        } else if (!soDienThoai.matches("^0\\d{9,10}$")) {
            model.addAttribute("errorMessage", "⚠️ Số điện thoại không hợp lệ (bắt đầu bằng 0 và có 10–11 số)!");
        } else {
            try {
                kh.setHoTen(hoTen.trim());
                kh.setSoDienThoai(soDienThoai.trim());

                if (file != null && !file.isEmpty()) {
                    String type = file.getContentType();
                    if (type != null && (type.equals("image/jpeg") || type.equals("image/png"))) {
                        kh.setAnhDaiDien(file.getBytes());
                    } else {
                        model.addAttribute("errorMessage", "⚠️ Ảnh đại diện chỉ chấp nhận định dạng JPG hoặc PNG!");
                    }
                }

                khachHangRepository.save(kh);
                model.addAttribute("successMessage", "✅ Cập nhật thông tin thành công!");

            } catch (IOException e) {
                model.addAttribute("errorMessage", "❌ Lỗi khi tải ảnh: " + e.getMessage());
            }
        }

        if (kh.getAnhDaiDien() != null)
            model.addAttribute("anhBase64", Base64.getEncoder().encodeToString(kh.getAnhDaiDien()));

        model.addAttribute("khachHang", kh);
        model.addAttribute("pageTitle", "Tài khoản cá nhân");
        return "customer/account";
    }

    // 🔒 Đổi mật khẩu (form thường)
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
        } else if (newPassword.length() < 6) {
            model.addAttribute("errorMessage", "⚠️ Mật khẩu mới phải ít nhất 6 ký tự!");
        } else if (!newPassword.equals(confirmPassword)) {
            model.addAttribute("errorMessage", "⚠️ Mật khẩu xác nhận không khớp!");
        } else {
            tk.setMatKhau(passwordEncoder.encode(newPassword));
            taiKhoanRepository.save(tk);
            model.addAttribute("successMessage", "🔑 Đổi mật khẩu thành công!");
        }

        KhachHang kh = khachHangRepository.findByEmail(email).orElse(null);
        if (kh != null && kh.getAnhDaiDien() != null)
            model.addAttribute("anhBase64", Base64.getEncoder().encodeToString(kh.getAnhDaiDien()));

        model.addAttribute("khachHang", kh);
        model.addAttribute("pageTitle", "Tài khoản cá nhân");
        return "customer/account";
    }

    // =========================== AJAX ===========================

    // 💾 Cập nhật thông tin cá nhân (AJAX)
    @PostMapping("/account/update-ajax")
    @ResponseBody
    public Map<String, String> updateAccountAjax(
            @RequestParam("HoTen") String hoTen,
            @RequestParam("SoDienThoai") String soDienThoai,
            @RequestParam(value = "anhDaiDienFile", required = false) MultipartFile file,
            HttpSession session) {

        Map<String, String> res = new HashMap<>();
        String email = SessionUtil.getCustomerEmail(session);
        if (email == null)
            return Map.of("status", "error", "message", "⚠️ Phiên đăng nhập đã hết hạn!");

        KhachHang kh = khachHangRepository.findByEmail(email).orElse(null);
        if (kh == null)
            return Map.of("status", "error", "message", "❌ Không tìm thấy khách hàng!");

        String nameRegex = "^[A-Za-zÀ-Ỹà-ỹĂăÂâÊêÔôƠơƯưĐđ\\s]{2,50}$";
        if (!hoTen.matches(nameRegex))
            return Map.of("status", "error", "message", "⚠️ Họ tên chỉ được chứa chữ cái và khoảng trắng!");
        if (!soDienThoai.matches("^0\\d{9,10}$"))
            return Map.of("status", "error", "message", "⚠️ Số điện thoại không hợp lệ!");

        try {
            kh.setHoTen(hoTen.trim());
            kh.setSoDienThoai(soDienThoai.trim());
            if (file != null && !file.isEmpty()) {
                String ct = file.getContentType();
                if (ct != null && (ct.equals("image/png") || ct.equals("image/jpeg"))) {
                    kh.setAnhDaiDien(file.getBytes());
                } else {
                    return Map.of("status", "error", "message", "⚠️ Ảnh chỉ chấp nhận PNG hoặc JPG!");
                }
            }
            khachHangRepository.save(kh);
            res.put("status", "success");
            res.put("message", "✅ Cập nhật thông tin thành công!");
        } catch (IOException e) {
            res.put("status", "error");
            res.put("message", "❌ Lỗi khi tải ảnh: " + e.getMessage());
        }
        return res;
    }

    // 🔒 Đổi mật khẩu (AJAX)
    @PostMapping("/account/change-password-ajax")
    @ResponseBody
    public Map<String, String> changePasswordAjax(
            @RequestParam("oldPassword") String oldPassword,
            @RequestParam("newPassword") String newPassword,
            @RequestParam("confirmPassword") String confirmPassword,
            HttpSession session) {

        String email = SessionUtil.getCustomerEmail(session);
        if (email == null)
            return Map.of("status", "error", "message", "⚠️ Phiên đăng nhập đã hết hạn!");

        TaiKhoan tk = taiKhoanRepository.findById(email).orElse(null);
        if (tk == null)
            return Map.of("status", "error", "message", "❌ Không tìm thấy tài khoản!");
        if (!passwordEncoder.matches(oldPassword, tk.getMatKhau()))
            return Map.of("status", "error", "message", "❌ Mật khẩu cũ không chính xác!");
        if (newPassword.length() < 6)
            return Map.of("status", "error", "message", "⚠️ Mật khẩu mới phải ít nhất 6 ký tự!");
        if (!newPassword.equals(confirmPassword))
            return Map.of("status", "error", "message", "⚠️ Mật khẩu xác nhận không khớp!");

        tk.setMatKhau(passwordEncoder.encode(newPassword));
        taiKhoanRepository.save(tk);
        return Map.of("status", "success", "message", "🔑 Đổi mật khẩu thành công!");
    }
}
