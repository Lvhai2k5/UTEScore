package vn.ute.utescore.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    /**
     * Gửi email reset password
     */
    public void sendPasswordResetEmail(String to, String newPassword, String userName) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(to);
            message.setSubject("Thông báo đặt lại mật khẩu - UTEScore");
            message.setText(createPasswordResetEmailContent(userName, newPassword));
            message.setFrom("noreply@utescore.edu.vn");
            
            mailSender.send(message);
            
            System.out.println("✅ Email đã được gửi thành công đến: " + to);
        } catch (Exception e) {
            System.err.println("❌ Lỗi gửi email: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Tạo nội dung email reset password
     */
    private String createPasswordResetEmailContent(String userName, String newPassword) {
        return "Kính gửi " + userName + ",\n\n" +
               "Chúng tôi đã nhận được yêu cầu đặt lại mật khẩu cho tài khoản của bạn.\n\n" +
               "Mật khẩu mới của bạn là: " + newPassword + "\n\n" +
               "Vui lòng đăng nhập và đổi mật khẩu ngay sau khi đăng nhập để đảm bảo bảo mật.\n\n" +
               "Nếu bạn không yêu cầu đặt lại mật khẩu, vui lòng liên hệ với quản trị viên ngay lập tức.\n\n" +
               "Trân trọng,\n" +
               "Đội ngũ UTEScore";
    }
    
    /**
     * Gửi email tạo tài khoản
     */
    public void sendAccountCreationEmail(String to, String userName, String role, String password) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(to);
            message.setSubject("Chào mừng đến với UTEScore - Tài khoản của bạn đã được tạo");
            message.setText(createAccountCreationEmailContent(userName, role, password));
            message.setFrom("noreply@utescore.edu.vn");
            
            mailSender.send(message);
            
            System.out.println("✅ Email tạo tài khoản đã được gửi thành công đến: " + to);
        } catch (Exception e) {
            System.err.println("❌ Lỗi gửi email tạo tài khoản: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Tạo nội dung email tạo tài khoản
     */
    private String createAccountCreationEmailContent(String userName, String role, String password) {
        return "Kính gửi " + userName + ",\n\n" +
               "Chào mừng bạn đến với hệ thống quản lý sân bóng UTEScore!\n\n" +
               "Tài khoản của bạn đã được tạo thành công với thông tin sau:\n\n" +
               "Vai trò: " + role + "\n" +
               "Vui lòng đăng nhập và đổi mật khẩu ngay sau khi đăng nhập để đảm bảo bảo mật tài khoản.\n\n" +
               "Nếu bạn có bất kỳ câu hỏi nào, vui lòng liên hệ với quản trị viên.\n\n" +
               "Trân trọng,\n" +
               "Đội ngũ UTEScore";
    }
}
