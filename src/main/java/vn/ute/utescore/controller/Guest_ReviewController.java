package vn.ute.utescore.controller;

import vn.ute.utescore.dto.Guest_ReviewDTO;
import vn.ute.utescore.service.Guest_ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
public class Guest_ReviewController {

    @Autowired
    private Guest_ReviewService reviewService;

    /**
     * 📋 Hiển thị tất cả đánh giá của 1 sân bóng cụ thể
     * @param pitchId mã sân bóng
     */
    @GetMapping("/pitches/{pitchId}/reviews")
    public String showReviewsByPitch(@PathVariable Long pitchId, Model model) {
        // Lấy danh sách review theo sân
        List<Guest_ReviewDTO> reviews = reviewService.getByPitchId(pitchId);
        // Tính điểm trung bình
        double avgRating = reviewService.averageRating(reviews);
        // Gửi dữ liệu sang View
        model.addAttribute("pitchId", pitchId);
        model.addAttribute("reviews", reviews);
        model.addAttribute("avgRating", avgRating);

        return "guest/reviews"; // gọi reviews.html
    }
}
