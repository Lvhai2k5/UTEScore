package vn.ute.utescore.service.impl;

import jakarta.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.ute.utescore.model.ThanhToan;
import vn.ute.utescore.model.ThueSan;
import vn.ute.utescore.model.KhachHang;
import vn.ute.utescore.repository.CustomerThanhToanRepository;
import vn.ute.utescore.service.CustomerEmailService;

import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class CustomerEmailServiceImpl implements CustomerEmailService {

    // ✅ Thay thế @Slf4j bằng Logger thủ công
    private static final Logger log = LoggerFactory.getLogger(CustomerEmailServiceImpl.class);

    private final JavaMailSender mailSender;
    private final CustomerThanhToanRepository thanhToanRepository;

    public CustomerEmailServiceImpl(JavaMailSender mailSender,
                                    CustomerThanhToanRepository thanhToanRepository) {
        this.mailSender = mailSender;
        this.thanhToanRepository = thanhToanRepository;
    }

    /** 🕐 Gửi mail nhắc lịch cho các đơn sắp đến giờ (0–120 phút tới) */
    @Transactional(readOnly = true)
    @Override
    public void sendUpcomingBookingReminders() {
        try {
            List<ThanhToan> upcomingPayments = thanhToanRepository.findUpcomingPayments();
            if (upcomingPayments == null || upcomingPayments.isEmpty()) {
                log.debug("📭 Không có đơn nào cần gửi nhắc lịch.");
                return;
            }

            int count = 0;
            for (ThanhToan tt : upcomingPayments) {
                sendBookingReminder(tt); // gọi hàm public bên dưới
                count++;
            }

            log.info("📩 Đã gửi nhắc lịch cho {} đơn đặt sân.", count);

        } catch (Exception e) {
            log.error("❌ Lỗi khi gửi nhắc sân hàng loạt: {}", e.getMessage(), e);
        }
    }

    /** 📨 Gửi mail nhắc lịch cho từng đơn cụ thể */
    @Override
    public void sendBookingReminder(ThanhToan tt) {
        try {
            ThueSan ts = tt.getThueSan();
            KhachHang kh = ts.getKhachHang();

            if (kh == null || kh.getEmail() == null || kh.getEmail().isBlank()) {
                log.warn("⚠️ Không tìm thấy email khách hàng cho mã đơn: {}", ts.getMaDonDat());
                return;
            }

            String ngay = ts.getNgayThue().toLocalDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            String gioBD = ts.getKhungGioBatDau().format(DateTimeFormatter.ofPattern("HH:mm"));
            String gioKT = ts.getKhungGioKetThuc().format(DateTimeFormatter.ofPattern("HH:mm"));
            BigDecimal soTien = tt.getSoTienNhan() != null ? tt.getSoTienNhan() : ts.getTongTien();

            String html = """
            <div style="font-family:'Segoe UI',Roboto,Arial,sans-serif;background:#f4f6f8;padding:30px;color:#333;">
              <div style="max-width:650px;margin:auto;background:#ffffff;border-radius:12px;
                          box-shadow:0 4px 12px rgba(0,0,0,0.08);overflow:hidden;">
                <div style="background:linear-gradient(90deg,#007BFF,#00C6FF);padding:20px;text-align:center;">
                  <h2 style="color:white;margin:0;font-size:22px;">⚽ UTEScore - Nhắc lịch đá sân</h2>
                </div>
                <div style="padding:30px;">
                  <p style="font-size:16px;">Xin chào <b>%s</b>,</p>
                  <p style="font-size:15px;line-height:1.6;">
                    Hệ thống gửi email này để <b>nhắc bạn trước 2 tiếng</b> về lịch đá sân của bạn tại 
                    <b style="color:#007BFF;">%s</b>.
                  </p>
                  <table style="width:100%%;border-collapse:collapse;margin:25px 0;font-size:14px;">
                    <tr><td style="padding:8px 12px;border-bottom:1px solid #eee;">📋 <b>Mã đơn:</b></td>
                        <td style="padding:8px 12px;border-bottom:1px solid #eee;">%s</td></tr>
                    <tr><td style="padding:8px 12px;border-bottom:1px solid #eee;">📍 <b>Tên sân:</b></td>
                        <td style="padding:8px 12px;border-bottom:1px solid #eee;">%s</td></tr>
                    <tr><td style="padding:8px 12px;border-bottom:1px solid #eee;">⏰ <b>Khung giờ:</b></td>
                        <td style="padding:8px 12px;border-bottom:1px solid #eee;">%s - %s</td></tr>
                    <tr><td style="padding:8px 12px;border-bottom:1px solid #eee;">📅 <b>Ngày thuê:</b></td>
                        <td style="padding:8px 12px;border-bottom:1px solid #eee;">%s</td></tr>
                    <tr><td style="padding:8px 12px;border-bottom:1px solid #eee;">💰 <b>Số tiền:</b></td>
                        <td style="padding:8px 12px;border-bottom:1px solid #eee;color:#007BFF;font-weight:600;">%s VNĐ</td></tr>
                    <tr><td style="padding:8px 12px;border-bottom:1px solid #eee;">📦 <b>Trạng thái:</b></td>
                        <td style="padding:8px 12px;border-bottom:1px solid #eee;">%s</td></tr>
                  </table>
                  <div style="background:#f0f8ff;border-left:4px solid #007BFF;padding:10px 15px;border-radius:6px;margin-top:20px;">
                    ⏰ Hệ thống gửi nhắc tự động <b>trước 2 tiếng</b> để bạn chuẩn bị tốt nhất cho trận đấu.
                  </div>
                  <p style="font-size:15px;line-height:1.6;margin-top:25px;">
                    Chúc bạn có trận đấu thật sôi động và nhiều bàn thắng! ⚽🔥<br>
                    Mọi thắc mắc vui lòng liên hệ đội ngũ <b>UTEScore</b> qua hotline 0344969875 hoặc email hỗ trợ.
                  </p>
                </div>
                <div style="background:#f8f9fa;padding:15px;text-align:center;font-size:12px;color:#666;">
                  <p style="margin:0;">Đây là email tự động, vui lòng không trả lời.</p>
                  <p style="margin:5px 0;">© 2025 UTEScore. All rights reserved.</p>
                </div>
              </div>
            </div>
            """.formatted(
                    kh.getHoTen(),
                    ts.getSanBong().getTenSan(),
                    ts.getMaDonDat(),
                    ts.getSanBong().getTenSan(),
                    gioBD, gioKT,
                    ngay,
                    soTien.toPlainString(),
                    tt.getTrangThaiThanhToan()
            );

            MimeMessage msg = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(msg, true, "UTF-8");
            helper.setTo(kh.getEmail());
            helper.setSubject("⚽ UTEScore - Nhắc lịch đá sân");
            helper.setText(html, true);
            mailSender.send(msg);

            log.info("📧 Đã gửi nhắc sân thành công cho: {}", kh.getEmail());

        } catch (Exception e) {
            log.error("❌ Lỗi khi gửi mail nhắc sân: {}", e.getMessage(), e);
        }
    }
}
