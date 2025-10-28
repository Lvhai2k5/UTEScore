package vn.ute.utescore.controller;

import vn.ute.utescore.model.KhuyenMai;
import vn.ute.utescore.service.Guest_NewsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class Guest_PromotionController {

    @Autowired
    private Guest_NewsService newsService;

    /**
     * 📋 Trang danh sách khuyến mãi
     */
    @GetMapping("/promotions")
    public String showPromotions(Model model) {

        // Lấy danh sách khuyến mãi đang hoạt động từ database
        model.addAttribute("promotions", newsService.getKhuyenMaiDangDienRa());

        return "guest/promotions"; // → gọi templates/guest/promotions.html
    }

    /**
     * 📄 Trang chi tiết khuyến mãi theo ID
     */
    @GetMapping("/promotions/{id}")
    public String showPromotionDetail(@PathVariable Integer id, Model model) {

        KhuyenMai km = newsService.getKhuyenMaiById(id);

        if (km == null) {
            model.addAttribute("error", "Không tìm thấy khuyến mãi này.");
            return "guest/error"; // hoặc redirect về danh sách cũng được
        }

        model.addAttribute("promotion", km);
        return "guest/promotion-detail"; // → gọi templates/guest/promotion-detail.html
    }
}
