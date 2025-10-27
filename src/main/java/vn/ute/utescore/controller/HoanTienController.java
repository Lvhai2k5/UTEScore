package vn.ute.utescore.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import vn.ute.utescore.model.HoanTien;
import vn.ute.utescore.repository.HoanTienRepository;

import java.time.LocalDateTime;
import java.util.Optional;

@Controller
@RequestMapping("/employee/refund")
public class HoanTienController {

    @Autowired
    private HoanTienRepository hoanTienRepo;

    // ✅ Hiển thị danh sách các yêu cầu hoàn tiền
    @GetMapping
    public String listRefunds(Model model) {
        model.addAttribute("refunds", hoanTienRepo.findAll());
        return "employee/refund-list";
    }

    // ✅ Xem chi tiết 1 yêu cầu hoàn tiền
    @GetMapping("/{id}")
    public String refundDetail(@PathVariable("id") Integer id, Model model) {
        Optional<HoanTien> hoanTien = hoanTienRepo.findById(id);
        if (hoanTien.isPresent()) {
            model.addAttribute("refund", hoanTien.get());
            return "employee/refund-detail";
        } else {
            return "redirect:/employee/refund";
        }
    }

    // ✅ Xác nhận hoàn tiền hoặc không hoàn
    @PostMapping("/{id}/process")
    public String processRefund(@PathVariable("id") Integer id,
                                @RequestParam("action") String action,
                                @RequestParam(value = "ghiChu", required = false) String ghiChu) {

        Optional<HoanTien> optionalHoanTien = hoanTienRepo.findById(id);
        if (optionalHoanTien.isPresent()) {
            HoanTien hoanTien = optionalHoanTien.get();
            hoanTien.setNgayXuLy(LocalDateTime.now());

            if ("approve".equals(action)) {
                hoanTien.setTrangThaiHoan("Hoàn tiền");
                hoanTien.setGhiChu(ghiChu != null ? ghiChu : "Hoàn tiền thành công");
            } else if ("reject".equals(action)) {
                hoanTien.setTrangThaiHoan("Từ chối hoàn tiền");
                hoanTien.setGhiChu(ghiChu != null ? ghiChu : "Không đồng ý hoàn tiền");
            }

            hoanTienRepo.save(hoanTien);
        }
        return "redirect:/employee/refund";
    }
}
