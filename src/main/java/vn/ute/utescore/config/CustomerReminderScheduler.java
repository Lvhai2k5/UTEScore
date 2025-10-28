package vn.ute.utescore.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import vn.ute.utescore.service.CustomerEmailService;

@Component
@EnableScheduling
public class CustomerReminderScheduler {

    // ✅ Thay thế cho @Slf4j
    private static final Logger log = LoggerFactory.getLogger(CustomerReminderScheduler.class);

    private final CustomerEmailService emailService;

    // ✅ Constructor thay cho @RequiredArgsConstructor
    public CustomerReminderScheduler(CustomerEmailService emailService) {
        this.emailService = emailService;
    }

    /**
     * 🕐 Tác vụ tự động chạy mỗi phút để gửi email nhắc lịch sân
     * Cron: 0 * * * * *  → chạy mỗi phút, theo múi giờ Việt Nam
     */
    @Scheduled(cron = "0 * * * * *", zone = "Asia/Ho_Chi_Minh")
    public void checkAndSendReminders() {
        try {
            emailService.sendUpcomingBookingReminders();
            log.info("✅ Job nhắc sân đã chạy thành công lúc {}", java.time.LocalTime.now());
        } catch (Exception e) {
            log.error("❌ Lỗi khi chạy job nhắc sân: {}", e.getMessage(), e);
        }
    }
}
