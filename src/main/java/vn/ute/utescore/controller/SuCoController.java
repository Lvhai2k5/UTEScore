// SuCoController.java
package vn.ute.utescore.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import vn.ute.utescore.model.*;
import vn.ute.utescore.service.*;

import jakarta.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/employee/incidents")
public class SuCoController {

    @Autowired private SuCoService suCoService;
    @Autowired private ThueSanService thueSanService;
    @Autowired private NhanVienService nhanVienService;

    // Trang danh sách đơn + form báo cáo
    @GetMapping
    public String listIncidents(Model model,
                                @RequestParam(required = false) String keyword,
                                HttpSession session) {

        // Lấy mã NV đăng nhập từ session (nếu bạn đã set khi login)
        // Ví dụ: session.setAttribute("CURRENT_EMPLOYEE_ID", 1);
        Integer currentEmpId = (Integer) session.getAttribute("CURRENT_EMPLOYEE_ID");
        if (currentEmpId == null) currentEmpId = 1; // fallback demo

        List<ThueSan> dsDon = thueSanService.getAll();
        if (keyword != null && !keyword.isEmpty()) {
            String kw = keyword.toLowerCase();
            dsDon = dsDon.stream()
                    .filter(d ->
                            String.valueOf(d.getMaDonDat()).contains(kw) ||
                            (d.getKhachHang()!=null && d.getKhachHang().getHoTen()!=null &&
                             d.getKhachHang().getHoTen().toLowerCase().contains(kw))
                    ).toList();
        }

        model.addAttribute("keyword", keyword);
        model.addAttribute("dsDon", dsDon);
        model.addAttribute("dsSuCo", suCoService.getAll());
        model.addAttribute("nhanVienDangNhapId", currentEmpId);
        return "employee/incidents";
    }
    
    @GetMapping("/search")
    public String searchTK(@RequestParam(required = false) String keyword, Model model) {
            model.addAttribute("dsDon", suCoService.searchByKeyword(keyword.trim()));
        model.addAttribute("keyword", keyword);
        return "employee/incidents";
    }

    // Lưu báo cáo
    @PostMapping("/report")
    public String reportIncident(@RequestParam("maDon") Integer maDon,
                                 @RequestParam("moTa") String moTa,
                                 @RequestParam("loaiSuCo") String loaiSuCo,
                                 @RequestParam("trangThai") String trangThai,
                                 @RequestParam("nhanVienId") Integer nhanVienId,
                                 Model model) {

        ThueSan thueSan = thueSanService.findById(maDon);
        NhanVien nhanVien = nhanVienService.findById(nhanVienId);

        SuCo suCo = new SuCo();
        suCo.setThueSan(thueSan);
        suCo.setMoTa(moTa);
        suCo.setLoaiSuCo(loaiSuCo);
        suCo.setTrangThai(trangThai);
        suCo.setNhanVien(nhanVien);
        suCo.setThoiGianBaoCao(LocalDateTime.now());

        suCoService.save(suCo);

        model.addAttribute("message", "✅ Báo cáo sự cố thành công! Hệ thống sẽ quay lại trang chính sau 3 giây...");
        return "employee/incidents_success";
    }
}
