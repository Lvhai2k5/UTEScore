package vn.ute.utescore.controller;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import vn.ute.utescore.model.CheckinLog;
import vn.ute.utescore.model.ThanhToan;
import vn.ute.utescore.model.ThueSan;
import vn.ute.utescore.repository.CheckinLogRepository;
import vn.ute.utescore.repository.ThanhToanRepository;
import vn.ute.utescore.repository.ThueSanRepository;
import vn.ute.utescore.service.BookingService;
import vn.ute.utescore.service.ThueSanService;

@Controller
public class EmployeeController {

	private final ThueSanService thueSanService;
    private final BookingService bookingService;
    private final ThueSanRepository thueSanRepository;
    private final CheckinLogRepository checkinLogRepository;
    private final ThanhToanRepository thanhToanRepository;
    private final SimpMessagingTemplate messagingTemplate; 

    public EmployeeController(ThueSanService thueSanService,
    							BookingService bookingService,
                              ThueSanRepository thueSanRepository,
                              CheckinLogRepository checkinLogRepository,
                              ThanhToanRepository thanhToanRepository,
                              SimpMessagingTemplate messagingTemplate) {
    	this.thueSanService=thueSanService;
        this.bookingService = bookingService;
        this.thueSanRepository = thueSanRepository;
        this.checkinLogRepository = checkinLogRepository;
        this.thanhToanRepository=thanhToanRepository;
        this.messagingTemplate=messagingTemplate;
    }

    /* ---------------------- TRANG NHÂN VIÊN ---------------------- */

    
    
    @GetMapping({"", "/"})
    public String home() {
        return "employee/employee";
    }

    @GetMapping("/employee")
    public String employee() {
        return "employee/employee";
    }

    /* ---------------------- QUẢN LÝ ĐƠN ĐẶT SÂN ---------------------- */

    @GetMapping("/employee/booking")
    public String viewBookings(Model model) {
        List<ThueSan> bookings = bookingService.findAll();
        model.addAttribute("bookings", bookings);
        model.addAttribute("selectedType", "");
        model.addAttribute("pageTitle", "UTEScore – Đơn đặt sân");
        return "employee/booking";
    }

    @GetMapping("employee/booking/filter")
    public String filterBookings(@RequestParam(required = false) String type, Model model) {
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
    public String searchBookings(@RequestParam(required = false) String keyword, Model model) {
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
    public String checkinSearch(@RequestParam(required = false) String keyword, Model model) {
        LocalDateTime from = LocalDateTime.now().minusHours(1); // mốc thời gian: chỉ lấy đơn chưa checkin trong tương lai gần

        List<ThueSan> donDatSans;

        if (keyword == null || keyword.trim().isEmpty()) {
            // 🟢 Trường hợp không nhập keyword → lấy toàn bộ đơn chưa checkin
            donDatSans = thueSanRepository.findNotCheckedIn(from);
        } else {
            // 🟢 Có keyword → lọc theo tên khách hoặc mã đơn + chưa checkin + thời gian
            donDatSans = thueSanService.searchFutureUncheckin(keyword);
            model.addAttribute("keyword", keyword);
        }

        model.addAttribute("donDatSans", donDatSans);
        return "employee/checkin";
    }

    // Danh sách chỉ những đơn CHƯA check-in và còn hiệu lực (từ 1h trước trở đi)
    @GetMapping("employee/checkin")
    public String listBookingsForCheckin(Model model) {
        LocalDateTime from = LocalDateTime.now().minusHours(1);
        List<ThueSan> donDatSans = thueSanRepository.findNotCheckedIn(from);
        model.addAttribute("donDatSans", donDatSans);
        return "employee/checkin";
    }

    // Trang hiển thị QR cho 1 đơn
    @GetMapping("employee/checkin/generate/{id}")
    public String generateQr(@PathVariable("id") Integer id, Model model) {
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
