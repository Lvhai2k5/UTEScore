package vn.ute.utescore.service.impl;

import vn.ute.utescore.dto.Guest_RegisterDTO;
import vn.ute.utescore.dto.Guest_LoginDTO;
import vn.ute.utescore.model.*;
import vn.ute.utescore.repository.*;
import vn.ute.utescore.service.Guest_AuthService;
import vn.ute.utescore.service.Guest_EmailService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class Guest_AuthServiceImpl implements Guest_AuthService {

    @Autowired private Guest_KhachHangRepository khRepo;
    @Autowired private Guest_TaiKhoanRepository tkRepo;
    @Autowired private Guest_RolesRepository roleRepo;
    @Autowired private Guest_EmailService emailService;

    // ✅ Lưu OTP tạm trong RAM
    private final Map<String, String> otpStore = new HashMap<>();

    @Override
    public void register(Guest_RegisterDTO dto) {

        if (tkRepo.findById(dto.getEmail()).isPresent())
            throw new RuntimeException("❌ Email đã tồn tại!");

        if (tkRepo.findBySoDienThoai(dto.getSoDienThoai()).isPresent())
            throw new RuntimeException("❌ Số điện thoại đã được sử dụng!");

        KhachHang kh = new KhachHang();
        kh.setHoTen(dto.getHoTen());
        kh.setSoDienThoai(dto.getSoDienThoai());
        kh.setEmail(dto.getEmail());
        kh.setTrangThai("HoatDong");
        kh.setNgayDangKy(LocalDateTime.now());
        khRepo.save(kh);

        Roles role = roleRepo.findByRoleName("KhachHang");

        TaiKhoan tk = new TaiKhoan();
        tk.setEmail(dto.getEmail());
        tk.setSoDienThoai(dto.getSoDienThoai());
        tk.setMatKhau(BCrypt.hashpw(dto.getMatKhau(), BCrypt.gensalt()));
        tk.setRole(role);
        tk.setTrangThai("HoatDong");
        tkRepo.save(tk);
    }

    @Override
    public TaiKhoan login(Guest_LoginDTO dto) {
        TaiKhoan tk = tkRepo.findBySoDienThoai(dto.getSoDienThoai())
                .orElseThrow(() -> new RuntimeException("❌ Số điện thoại không tồn tại!"));

        if (!BCrypt.checkpw(dto.getMatKhau(), tk.getMatKhau()))
            throw new RuntimeException("❌ Mật khẩu không chính xác!");

        return tk;
    }

    // ==================== QUÊN MẬT KHẨU ====================

    @Override
    public void sendResetOTP(String soDienThoai, String email) {

        // ✅ Kiểm tra đúng tài khoản
        TaiKhoan tk = tkRepo.findBySoDienThoai(soDienThoai)
                .orElseThrow(() -> new RuntimeException("❌ Số điện thoại chưa đăng ký tài khoản!"));

        if (!tk.getEmail().equals(email))
            throw new RuntimeException("❌ Email không khớp với tài khoản!");

        // ✅ Tạo OTP
        String otp = String.valueOf(new Random().nextInt(900000) + 100000);
        otpStore.put(email, otp);

        // ✅ Gửi Email
        String subject = "Mã xác thực đặt lại mật khẩu - UTEScore";
        String body = "Mã xác nhận của bạn là: <b>" + otp + "</b><br>Không chia sẻ cho ai.";

        emailService.sendEmail(email, subject, body);
    }

    @Override
    public boolean verifyOTP(String email, String otp) {
        return otp != null && otp.equals(otpStore.get(email));
    }

    @Override
    public void updatePassword(String email, String newPassword) {
        TaiKhoan tk = tkRepo.findById(email)
                .orElseThrow(() -> new RuntimeException("❌ Không tìm thấy tài khoản!"));

        tk.setMatKhau(BCrypt.hashpw(newPassword, BCrypt.gensalt()));
        tkRepo.save(tk);

        // ✅ Xóa OTP sau khi hoàn tất
        otpStore.remove(email);
    }
}
