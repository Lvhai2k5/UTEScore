package vn.ute.utescore.controller;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpSession;
import vn.ute.utescore.model.NhanVien;
import vn.ute.utescore.model.ThueSan;
import vn.ute.utescore.repository.ThueSanRepository;
import vn.ute.utescore.utils.SessionUtil;
import vn.ute.utescore.repository.NhanVienRepository;
import vn.ute.utescore.repository.SanBongRepository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

@Controller
public class ScheduleController {

	private final ThueSanRepository thueSanRepository;
    private final SanBongRepository sanBongRepository;
    private final NhanVienRepository nhanVienRepository;

    // ✅ Constructor injection (ổn định, dễ test, không cần @Autowired)
    public ScheduleController(ThueSanRepository thueSanRepository,
                          SanBongRepository sanBongRepository,
                          NhanVienRepository nhanVienRepository) {
        this.thueSanRepository = thueSanRepository;
        this.sanBongRepository = sanBongRepository;
        this.nhanVienRepository = nhanVienRepository;
    }

    // 📅 Chọn 1 ngày và sân để lọc lại danh sách khung giờ
    @GetMapping("/employee/schedule")
    public String showSchedule(
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate date,
            @RequestParam(required = false) Integer sanId,
            HttpSession session,
            Model model) {
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
        // Mặc định là hôm nay
        if (date == null) {
            date = LocalDate.now();
        }

        // Danh sách sân cho combobox
        model.addAttribute("sanList", sanBongRepository.findAll());
        model.addAttribute("selectedDate", date);

        // Nếu chưa chọn sân thì chỉ hiển thị form
        if (sanId == null) {
            model.addAttribute("slots", Collections.emptyList());
            return "employee/schedule";
        }

        // Lấy các đơn đặt của sân trong ngày đã chọn
        List<ThueSan> bookedList = thueSanRepository.findBySanBong_MaSanAndNgayThueBetween(
                sanId,
                date.atStartOfDay(),
                date.atTime(23, 59, 59)
        );

        // Tạo danh sách khung giờ từ 6h - 22h
        List<Map<String, Object>> slots = new ArrayList<>();
        for (int hour = 6; hour < 22; hour++) {
            LocalTime start = LocalTime.of(hour, 0);
            LocalTime end = LocalTime.of(hour + 1, 0);

            boolean booked = bookedList.stream().anyMatch(ts ->
                    !start.isAfter(ts.getKhungGioKetThuc()) &&
                    !end.isBefore(ts.getKhungGioBatDau())
            );

            Map<String, Object> slot = new HashMap<>();
            slot.put("time", String.format("%02d:00 - %02d:00", hour, hour + 1));
            slot.put("status", booked ? "Đã đặt" : "Trống");
            slots.add(slot);
        }

        model.addAttribute("slots", slots);
        model.addAttribute("selectedSan", sanBongRepository.findById(sanId).orElse(null));
        return "employee/schedule";
    }
}
