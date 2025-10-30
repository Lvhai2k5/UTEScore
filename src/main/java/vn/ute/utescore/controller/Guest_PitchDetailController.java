package vn.ute.utescore.controller;

import vn.ute.utescore.dto.Guest_PitchDTO;
import vn.ute.utescore.dto.Guest_ReviewDTO;
import vn.ute.utescore.model.GiaThue;
import vn.ute.utescore.service.Guest_PitchService;
import vn.ute.utescore.service.Guest_PriceService;
import vn.ute.utescore.service.Guest_ReviewService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
public class Guest_PitchDetailController {

    @Autowired
    private Guest_PitchService pitchService;

    @Autowired
    private Guest_ReviewService reviewService;
    @Autowired private Guest_PriceService priceService;

    /**
     * ✅ Hiển thị chi tiết 1 sân bóng + danh sách đánh giá liên quan
     */
//    @GetMapping("/pitches/{id}")
//    public String showPitchDetail(@PathVariable("id") Long id, Model model) {
//        // ⚽ Lấy danh sách sân và tìm sân có id tương ứng
//        List<Guest_PitchDTO> all = pitchService.getAllActive();
//        Guest_PitchDTO pitch = all.stream()
//                .filter(p -> p.getSanId().equals(id))
//                .findFirst()
//                .orElse(null);
//
//        // ⭐ Lấy đánh giá cho sân này
//        List<Guest_ReviewDTO> reviews = reviewService.getByPitchId(id);
//        double avg = reviewService.averageRating(reviews);
//
//        model.addAttribute("pitch", pitch);
//        model.addAttribute("reviews", reviews);
//        model.addAttribute("avgRating", avg);
//        return "guest/pitch-detail"; // view hiển thị chi tiết
//    }
}
