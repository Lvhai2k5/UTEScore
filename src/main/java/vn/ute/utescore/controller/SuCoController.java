package vn.ute.utescore.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import vn.ute.utescore.model.*;
import vn.ute.utescore.repository.NhanVienRepository;
import vn.ute.utescore.service.*;
import vn.ute.utescore.utils.SessionUtil;

import jakarta.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/employee/incidents")
public class SuCoController {

    @Autowired private SuCoService suCoService;
    @Autowired private ThueSanService thueSanService;
    @Autowired private NhanVienRepository nhanVienRepository;

    // ===== Danh sách sự cố & đơn thuê =====
    @GetMapping
    public String listIncidents(Model model,
                                @RequestParam(required = false) String keyword,
                                HttpSession session) {

        String email = SessionUtil.getCustomerEmail(session);
        if (email == null) {
            return "redirect:/login";
        }

        Optional<NhanVien> optionalNV = nhanVienRepository.findByEmail(email);
        if (optionalNV.isEmpty()) {
            model.addAttribute("nhanVienDangNhap", null);
            return "redirect:/login";
        }

        NhanVien nhanVien = optionalNV.get();
        model.addAttribute("nhanVienDangNhap", nhanVien);

        // Lấy danh sách đơn thuê sân
        List<ThueSan> dsDon = thueSanService.getAll();
        if (keyword != null && !keyword.trim().isEmpty()) {
            String kw = keyword.toLowerCase();
            dsDon = dsDon.stream()
                    .filter(d ->
                            String.valueOf(d.getMaDonDat()).contains(kw) ||
                            (d.getKhachHang() != null && d.getKhachHang().getHoTen() != null &&
                             d.getKhachHang().getHoTen().toLowerCase().contains(kw))
                    ).toList();
        }

        model.addAttribute("keyword", keyword);
        model.addAttribute("dsDon", dsDon);
        model.addAttribute("dsSuCo", suCoService.getAll());
        model.addAttribute("nhanVienDangNhapTen", nhanVien.getFullName());

        return "employee/incidents";
    }

    // ===== Lưu báo cáo sự cố =====
    @PostMapping("/report")
    public String reportIncident(@RequestParam("maDon") Integer maDon,
                                 @RequestParam("moTa") String moTa,
                                 @RequestParam("loaiSuCo") String loaiSuCo,
                                 @RequestParam("trangThai") String trangThai,
                                 HttpSession session,
                                 Model model) {

        String email = SessionUtil.getCustomerEmail(session);
        if (email == null) {
            return "redirect:/login";
        }

        Optional<NhanVien> optionalNV = nhanVienRepository.findByEmail(email);
        if (optionalNV.isEmpty()) {
            model.addAttribute("message", "❌ Không tìm thấy nhân viên đăng nhập.");
            return "employee/incidents";
        }

        NhanVien nhanVien = optionalNV.get();
        ThueSan thueSan = thueSanService.findById(maDon);

        SuCo suCo = new SuCo();
        suCo.setThueSan(thueSan);
        suCo.setMoTa(moTa);
        suCo.setLoaiSuCo(loaiSuCo);
        suCo.setTrangThai(trangThai);
        suCo.setNhanVien(nhanVien);
        suCo.setThoiGianBaoCao(LocalDateTime.now());

        suCoService.save(suCo);

        model.addAttribute("message", "✅ Báo cáo sự cố thành công!");
        return "redirect:/employee/incidents";
    }
    @GetMapping("/search")
    public String searchIncidents(@RequestParam(required = false) String keyword,
                                  Model model,
                                  HttpSession session) {

    	String email = SessionUtil.getCustomerEmail(session);
    	if (email == null) {
    	    return "redirect:/login";
    	}

    	Optional<NhanVien> optionalNV = nhanVienRepository.findByEmail(email);
    	if (optionalNV.isEmpty()) {
    	    model.addAttribute("nhanVienDangNhap", null);
    	    return "redirect:/login";
    	}

    	NhanVien nhanVien = optionalNV.get();
    	model.addAttribute("nhanVienDangNhap", nhanVien);

    	// ✅ Lấy danh sách đơn thuê sân
    	List<ThueSan> dsDon = thueSanService.getAll();

    	if (keyword != null && !keyword.trim().isEmpty()) {
    	    String kw = keyword.toLowerCase();

    	    dsDon = dsDon.stream()
    	            .filter(d ->
    	                    // Lọc theo mã đơn
    	                    String.valueOf(d.getMaDonDat()).contains(kw)
    	                    // Lọc theo tên khách hàng
    	                    || (d.getKhachHang() != null && d.getKhachHang().getHoTen() != null &&
    	                        d.getKhachHang().getHoTen().toLowerCase().contains(kw))
    	                    // Lọc theo tên sân bóng (nếu có)
    	                    || (d.getSanBong() != null && d.getSanBong().getTenSan() != null &&
    	                        d.getSanBong().getTenSan().toLowerCase().contains(kw))
    	            )
    	            .toList();
    	}

    	// ✅ Truyền dữ liệu xuống view
    	model.addAttribute("keyword", keyword);
    	model.addAttribute("dsDon", dsDon);
    	model.addAttribute("dsSuCo", suCoService.getAll());
    	model.addAttribute("nhanVienDangNhapTen", nhanVien.getFullName());

    	return "employee/incidents";

    }
}
