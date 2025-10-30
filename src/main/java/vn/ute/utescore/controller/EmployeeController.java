package vn.ute.utescore.controller;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpSession;
import vn.ute.utescore.model.CheckinLog;
import vn.ute.utescore.model.NhanVien;
import vn.ute.utescore.model.ThanhToan;
import vn.ute.utescore.model.ThueSan;
import vn.ute.utescore.repository.CheckinLogRepository;
import vn.ute.utescore.repository.NhanVienRepository;
import vn.ute.utescore.repository.TaiKhoanRepository;
import vn.ute.utescore.repository.ThanhToanRepository;
import vn.ute.utescore.repository.ThueSanRepository;
import vn.ute.utescore.service.BookingService;
import vn.ute.utescore.service.ThueSanService;
import vn.ute.utescore.utils.SessionUtil;

@Controller
public class EmployeeController {

	private final ThueSanService thueSanService;
    private final BookingService bookingService;
    private final ThueSanRepository thueSanRepository;
    private final CheckinLogRepository checkinLogRepository;
    private final ThanhToanRepository thanhToanRepository;
    private final NhanVienRepository nhanVienRepository;
    private final SimpMessagingTemplate messagingTemplate; 

    public EmployeeController(ThueSanService thueSanService,
    							BookingService bookingService,
                              ThueSanRepository thueSanRepository,
                              CheckinLogRepository checkinLogRepository,
                              ThanhToanRepository thanhToanRepository,
                              NhanVienRepository nhanVienRepository,
                              SimpMessagingTemplate messagingTemplate) {
    	this.thueSanService=thueSanService;
        this.bookingService = bookingService;
        this.thueSanRepository = thueSanRepository;
        this.checkinLogRepository = checkinLogRepository;
        this.thanhToanRepository=thanhToanRepository;
        this.nhanVienRepository=nhanVienRepository;
        this.messagingTemplate=messagingTemplate;
    }

    /* ---------------------- TRANG NHÂN VIÊN ---------------------- */

   
    @GetMapping("/employee")
    public String employee(HttpSession session,Model model) {
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

            model.addAttribute("pageTitle", "UTEScore – Trang nhân viên");
            return "employee/employee";
  
    }

    /* ---------------------- QUẢN LÝ ĐƠN ĐẶT SÂN ---------------------- */

    @GetMapping("/employee/booking")
    public String viewBookings(HttpSession session,Model model) {
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

        List<ThueSan> bookings = bookingService.findAll();
        model.addAttribute("bookings", bookings);
        model.addAttribute("selectedType", "");
        model.addAttribute("pageTitle", "UTEScore – Đơn đặt sân");
        return "employee/booking";
    }

    @GetMapping("employee/booking/filter")
    public String filterBookings(@RequestParam(required = false) String type, HttpSession session,Model model) {
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

    	List<ThueSan> bookings;

        if (type == null || type.isEmpty()) {
            bookings = bookingService.findAll();
        } else {
            bookings = bookingService.findByLoaiThanhToan(type);
        }

        model.addAttribute("bookings", bookings);
        model.addAttribute("selectedType", type);
        model.addAttribute("pageTitle", "UTEScore – Lọc đơn đặt sân");
        return "employee/booking";
    }

    @GetMapping("employee/booking/search")
    public String searchBookings(@RequestParam(required = false) String keyword,HttpSession session, Model model) {
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
    	if (keyword == null || keyword.trim().isEmpty()) {
            model.addAttribute("bookings", bookingService.findAll());
        } else {
            model.addAttribute("bookings", bookingService.searchByKeyword(keyword.trim()));
        }
        model.addAttribute("keyword", keyword);
        return "employee/booking";
    }

    /* ========================= CHỨC NĂNG CHECK-IN ========================= */

    @GetMapping("employee/checkin/search")
    public String checkinSearch(@RequestParam(required = false) String keyword,HttpSession session, Model model) {
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
    	LocalDateTime from = LocalDateTime.now().minusHours(1); // mốc thời gian: chỉ lấy đơn chưa checkin trong tương lai gần

        List<ThueSan> donDatSans;

        if (keyword == null || keyword.trim().isEmpty()) {
            donDatSans = thueSanRepository.findNotCheckedIn(from);
        } else {
            donDatSans = thueSanService.searchFutureUncheckin(keyword);
            model.addAttribute("keyword", keyword);
        }

        model.addAttribute("donDatSans", donDatSans);
        return "employee/checkin";
    }

    // Danh sách chỉ những đơn CHƯA check-in và còn hiệu lực (từ 1h trước trở đi)
    @GetMapping("employee/checkin")
    public String listBookingsForCheckin(HttpSession session,Model model) {
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
        }LocalDateTime from = LocalDate.now().atStartOfDay(); // 00:00:00
        List<ThueSan> donDatSans = thueSanRepository.findNotCheckedInWithStatus(from,"Thành công");
        model.addAttribute("donDatSans", donDatSans);
        return "employee/checkin";
    }

    // Trang hiển thị QR cho 1 đơn
    @GetMapping("employee/checkin/generate/{id}")
    public String generateQr(@PathVariable("id") Integer id,HttpSession session, Model model) {
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
        model.addAttribute("maDonDat", id);
        return "employee/checkin_qr";
    }

 // ✅ Khi khách quét QR
    @GetMapping("/employee/checkin/{id}")
    public String processCheckin(@PathVariable("id") Integer id, Model model) {
        ThueSan don = thueSanRepository.findById(id).orElse(null);
        if (don == null) {
            model.addAttribute("message", "❌ Không tìm thấy đơn đặt sân #" + id);
            return "employee/checkin_success";
        }

        Optional<CheckinLog> existing = checkinLogRepository.findTopByThueSan_MaDonDatOrderByMaCheckinDesc(id);

        CheckinLog log;

        if (existing.isPresent()) {
            log = existing.get();
            log.setGhiChu("1");
            log.setNgayCheckin(LocalDateTime.now());
        } else {
            log = new CheckinLog(don, LocalDateTime.now(), "1");
        }
        checkinLogRepository.save(log);
        
     // ✅ Cập nhật trạng thái thanh toán
        List<ThanhToan> payments = thanhToanRepository.findByThueSan_MaDonDat(id);
        for (ThanhToan tt : payments) {
            if ("Đặt cọc".equals(tt.getLoaiThanhToan())) {
                tt.setLoaiThanhToan("Hoàn tất");
                thanhToanRepository.save(tt);
            }
        }
        // 🔥 Gửi thông báo realtime tới tất cả nhân viên
        messagingTemplate.convertAndSend("/topic/checkin-updates", "updated");

        model.addAttribute("message", "✅ Check-in thành công cho đơn #" + id);
        return "employee/checkin_success";
    }
}
