package vn.ute.utescore.controller;

import vn.ute.utescore.dto.Guest_PitchDTO;
import vn.ute.utescore.service.Guest_PitchService;
import vn.ute.utescore.service.Guest_ReviewService;
import vn.ute.utescore.dto.Guest_ReviewDTO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class Guest_PitchController {

    private static final Logger logger = LoggerFactory.getLogger(Guest_PitchController.class);

    @Autowired
    private Guest_PitchService pitchService;   // ✅ Inject interface, không dùng impl

    @Autowired
    private Guest_ReviewService reviewService; // ✅ Inject interface

    /**
     * 📄 Hiển thị danh sách sân bóng
     */
    @GetMapping("/pitches")
    public String showPitches(Model model) {
        logger.info("⚙️ PitchController.showPitches() được gọi!");

        // Lấy danh sách sân hoạt động
        List<Guest_PitchDTO> pitches = pitchService.getAllActive();
        logger.info("🎯 Tổng số sân đang hoạt động: {}", pitches.size());

        // Lấy review mẫu (nếu muốn hiển thị ở giao diện)
        List<Guest_ReviewDTO> reviews = reviewService.getSampleReviews();

        // Đưa dữ liệu ra view
        model.addAttribute("pitches", pitches);
        model.addAttribute("reviews", reviews);

        return "guest/pitches"; // Trả về template pitches.html
    }
}
