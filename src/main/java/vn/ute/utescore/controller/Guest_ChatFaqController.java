package vn.ute.utescore.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.*;

@RestController
public class Guest_ChatFaqController {

    @GetMapping("/api/faqs")
    public List<Map<String, String>> getFaqs() {
        List<Map<String, String>> faqs = new ArrayList<>();

        faqs.add(Map.of("question", "⏰ Giờ mở cửa của sân bóng là khi nào?",
                        "answer", "UTEScore mở cửa từ 5h sáng đến 23h tối mỗi ngày."));

        faqs.add(Map.of("question", "💰 Giá thuê sân là bao nhiêu?",
                        "answer", "Sân 5 người: 200.000đ/h • Sân 7 người: 300.000đ/h • Sân 11 người: 500.000đ/h."));

        faqs.add(Map.of("question", "🧺 Sân có bãi giữ xe và phòng tắm không?",
                        "answer", "Có, sân UTEScore có bãi giữ xe rộng, phòng tắm và khu thay đồ sạch sẽ."));

        faqs.add(Map.of("question", "📞 Làm sao để đặt sân?",
                        "answer", "Bạn có thể đặt sân trực tiếp trên website hoặc liên hệ hotline 0909.888.999."));

        return faqs;
    }
}
