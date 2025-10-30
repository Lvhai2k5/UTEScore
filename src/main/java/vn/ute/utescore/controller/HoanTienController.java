package vn.ute.utescore.controller;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpSession;
import vn.ute.utescore.model.HoanTien;
import vn.ute.utescore.model.NhanVien;
import vn.ute.utescore.repository.HoanTienRepository;
import vn.ute.utescore.repository.NhanVienRepository;
import vn.ute.utescore.repository.ThanhToanRepository;
import vn.ute.utescore.utils.SessionUtil;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/employee/refund")
public class HoanTienController {

	 private final HoanTienRepository hoanTienRepo;
	    private final NhanVienRepository nhanVienRepository;
	    private final ThanhToanRepository thanhToanRepository;

	    // ✅ Constructor injection (không cần @Autowired)
	    public HoanTienController(HoanTienRepository hoanTienRepo,
	                              NhanVienRepository nhanVienRepository,
	                              ThanhToanRepository thanhToanRepository) {
	        this.hoanTienRepo = hoanTienRepo;
	        this.nhanVienRepository = nhanVienRepository;
	        this.thanhToanRepository=thanhToanRepository;
	    }
    
    // ✅ Hiển thị danh sách các yêu cầu hoàn tiền
    @GetMapping
    public String listRefunds(HttpSession session,Model model) {
    	String email = SessionUtil.getCustomerEmail(session);

        if (email != null) {
            Optional<NhanVien> optionalNV = nhanVienRepository.findByEmail(email);

            if (optionalNV.isPresent()) {
                NhanVien nhanVien = optionalNV.get();
                model.addAttribute("nhanVienDangNhap", nhanVien);
            } else {
                model.addAttribute("nhanVienDangNhap", null);
            }
        } else {
                return "redirect:/login";
        }
        List<HoanTien> refunds = hoanTienRepo.findByTrangThaiHoan("Chờ duyệt");
        model.addAttribute("refunds", refunds);
        return "employee/refund-list";
    }

    // ✅ Xem chi tiết 1 yêu cầu hoàn tiền
    @GetMapping("/{id}")
    public String refundDetail(@PathVariable("id") Integer id,HttpSession  session, Model model) {
    	String email = SessionUtil.getCustomerEmail(session);

        if (email != null) {
            Optional<NhanVien> optionalNV = nhanVienRepository.findByEmail(email);

            if (optionalNV.isPresent()) {
                NhanVien nhanVien = optionalNV.get();
                model.addAttribute("nhanVienDangNhap", nhanVien);
            } else {
                model.addAttribute("nhanVienDangNhap", null);
            }
        } else {
                return "redirect:/login";
        }
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
                hoanTien.setTrangThaiHoan("Đã hoàn");
                hoanTien.setGhiChu(ghiChu != null ? ghiChu : "Hoàn tiền thành công");
            } else if ("reject".equals(action)) {
                hoanTien.setTrangThaiHoan("Từ chối");
                hoanTien.setGhiChu(ghiChu != null ? ghiChu : "Không đồng ý hoàn tiền");
            }

            hoanTienRepo.save(hoanTien);
        }
        return "redirect:/employee/refund";
    }
}
