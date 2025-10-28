package vn.ute.utescore.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import vn.ute.utescore.dto.Guest_ForgotPasswordDTO;
import vn.ute.utescore.dto.Guest_OtpVerifyDTO;
import vn.ute.utescore.dto.Guest_ResetPasswordDTO;
import vn.ute.utescore.service.Guest_AuthService;

@Controller
public class Guest_ForgotPasswordController {

    @Autowired
    private Guest_AuthService authService;

    // B1: Nhập email + sdt
    @GetMapping("/forgot")
    public String forgotPage(Model model) {
        model.addAttribute("dto", new Guest_ForgotPasswordDTO());
        return "guest/forgot-password";
    }

    @PostMapping("/forgot")
    public String sendOtp(@ModelAttribute("dto") Guest_ForgotPasswordDTO dto, Model model) {
        try {
            authService.sendResetOTP(dto.getSoDienThoai(), dto.getEmail());
            model.addAttribute("dto", new Guest_OtpVerifyDTO(dto.getEmail(), "")); 
            return "guest/verify-otp";
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            return "guest/forgot-password";
        }
    }

    // B2: Kiểm tra OTP
    @PostMapping("/verify-otp")
    public String verifyOtp(@ModelAttribute("dto") Guest_OtpVerifyDTO dto, Model model) {
        if (!authService.verifyOTP(dto.getEmail(), dto.getOtp())) {
            model.addAttribute("error", "Mã OTP không đúng!");
            return "guest/verify-otp";
        }
        model.addAttribute("dto", new Guest_ResetPasswordDTO(dto.getEmail(), ""));
        return "guest/reset-password";
    }

    // B3: Đổi mật khẩu mới
    @PostMapping("/reset-password")
    public String resetPassword(@ModelAttribute("dto") Guest_ResetPasswordDTO dto) {
        authService.updatePassword(dto.getEmail(), dto.getNewPassword());
        return "redirect:/login?resetSuccess";
    }
}
