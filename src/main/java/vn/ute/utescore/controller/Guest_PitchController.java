package vn.ute.utescore.controller;

import vn.ute.utescore.dto.Guest_PitchDTO;
import vn.ute.utescore.dto.Guest_ReviewDTO;
import vn.ute.utescore.service.Guest_PitchService;
import vn.ute.utescore.service.Guest_ReviewService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class Guest_PitchController {

    private static final Logger logger = LoggerFactory.getLogger(Guest_PitchController.class);

    @Autowired
    private Guest_PitchService pitchService;

    @Autowired
    private Guest_ReviewService reviewService;

    /**
     * 🏟️ Hiển thị danh sách sân bóng (GET)
     */
    @GetMapping("/pitches")
    public String showPitches(Model model) {
        logger.info("⚙️ Guest_PitchController.showPitches() được gọi");

        List<Guest_PitchDTO> pitches = pitchService.getAllActive();
        List<Guest_ReviewDTO> reviews = reviewService.getSampleReviews();

        model.addAttribute("pitches", pitches);
        model.addAttribute("reviews", reviews);
        model.addAttribute("pageTitle", "⚽ Danh sách Sân Bóng | UTEScore");

        return "guest/pitches";
    }

    /**
     * 🔍 Lọc sân bóng theo điều kiện (POST)
     */
    @PostMapping("/pitches")
    public String filterPitches(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String loaiSan,
            @RequestParam(required = false) String trangThai,
            @RequestParam(required = false) String gioBatDau,
            @RequestParam(required = false) String gioKetThuc,
            Model model) {

        logger.info("🔍 Đang lọc sân bóng với: keyword='{}', loaiSan='{}', trangThai='{}', gioBatDau='{}', gioKetThuc='{}'",
                keyword, loaiSan, trangThai, gioBatDau, gioKetThuc);

        // Gọi service để lấy danh sách sân theo điều kiện
        List<Guest_PitchDTO> filteredPitches = pitchService.searchPitches(keyword, loaiSan, trangThai, gioBatDau, gioKetThuc);

        // Lấy review mẫu để hiển thị (nếu cần)
        List<Guest_ReviewDTO> reviews = reviewService.getSampleReviews();

        // Gửi lại dữ liệu ra view
        model.addAttribute("pitches", filteredPitches);
        model.addAttribute("reviews", reviews);

        // Giữ lại giá trị form lọc
        model.addAttribute("keyword", keyword);
        model.addAttribute("loaiSan", loaiSan);
        model.addAttribute("trangThai", trangThai);
        model.addAttribute("gioBatDau", gioBatDau);
        model.addAttribute("gioKetThuc", gioKetThuc);
        model.addAttribute("pageTitle", "⚽ Danh sách Sân Bóng | UTEScore");

        return "guest/pitches";
    }
}
