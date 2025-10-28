package vn.ute.utescore.service;

import vn.ute.utescore.dto.Guest_RegisterDTO;
import vn.ute.utescore.dto.Guest_LoginDTO;
import vn.ute.utescore.model.TaiKhoan;

public interface Guest_AuthService {

    void register(Guest_RegisterDTO dto);

    TaiKhoan login(Guest_LoginDTO dto);

    // ✅ Quên mật khẩu: Gửi OTP
    void sendResetOTP(String soDienThoai, String email);

    // ✅ Xác thực OTP
    boolean verifyOTP(String email, String otp);

    // ✅ Đặt mật khẩu mới
    void updatePassword(String email, String newPassword);
}
