package vn.ute.utescore.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import vn.ute.utescore.model.*;
import vn.ute.utescore.repository.*;
import vn.ute.utescore.utils.SessionUtil;

import java.math.BigDecimal;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * 🎯 Controller xử lý luồng ĐẶT SÂN – XÁC NHẬN – THANH TOÁN VNPay
 * Giữ đơn trong 10 phút, cập nhật trạng thái sau khi thanh toán
 */
@Controller
@RequestMapping("/customer")
public class CustomerBookingController {

    private final CustomerSanBongRepository sanBongRepository;
    private final CustomerSystemConfigurationsRepository systemConfigRepo;
    private final CustomerGiaThueRepository giaThueRepository;
    private final CustomerKhachHangRepository khachHangRepository;
    private final CustomerThueSanRepository thueSanRepository;
    public CustomerBookingController(
            CustomerSanBongRepository sanBongRepository,
            CustomerSystemConfigurationsRepository systemConfigRepo,
            CustomerGiaThueRepository giaThueRepository,
            CustomerKhachHangRepository khachHangRepository,
            CustomerThueSanRepository thueSanRepository) {
        this.sanBongRepository = sanBongRepository;
        this.systemConfigRepo = systemConfigRepo;
        this.giaThueRepository = giaThueRepository;
        this.khachHangRepository = khachHangRepository;
        this.thueSanRepository = thueSanRepository;
    }


    /* ===========================================================
     * 🏟️ [GET] Trang đặt sân
     * =========================================================== */
    @GetMapping("/booking")
    public String showBookingPage(@RequestParam(required = false) Integer pitch,
                                  Model model, HttpSession session) {
        String email = SessionUtil.getCustomerEmail(session);
        if (email == null) return "redirect:/login";

        Optional<KhachHang> khOpt = khachHangRepository.findByEmail(email);
        khOpt.ifPresent(kh -> model.addAttribute("customerPhone", kh.getSoDienThoai()));

        if (pitch == null) {
            model.addAttribute("error", "Không tìm thấy sân bóng hợp lệ!");
            return "customer/booking";
        }

        Optional<SanBong> optSan = sanBongRepository.findById(pitch);
        if (optSan.isEmpty()) {
            model.addAttribute("error", "Không tìm thấy sân bóng!");
            return "customer/booking";
        }

        SanBong san = optSan.get();

        model.addAttribute("pitchId", san.getMaSan());
        model.addAttribute("pitchName", san.getTenSan());
        model.addAttribute("pitchType", san.getLoaiSan());
        model.addAttribute("pitchArea", san.getKhuVuc());
        model.addAttribute("pitchStatus", san.getTrangThai());
        model.addAttribute("pitchSize", getPitchSize(san.getLoaiSan()));
        model.addAttribute("pitchOpen", san.getGioMoCua() != null ? san.getGioMoCua().toString() : "06:00");
        model.addAttribute("pitchClose", san.getGioDongCua() != null ? san.getGioDongCua().toString() : "22:00");
        model.addAttribute("pitchImg",
                (san.getHinhAnh() != null && !san.getHinhAnh().isBlank())
                        ? san.getHinhAnh()
                        : "https://cdn-icons-png.flaticon.com/512/5341/5341961.png");

        List<GiaThue> priceList = giaThueRepository.findActiveByLoaiSan(san.getLoaiSan(), "đang áp dụng");
        DateTimeFormatter timeFmt = DateTimeFormatter.ofPattern("HH:mm");
        List<Map<String, Object>> priceData = new ArrayList<>();
        for (GiaThue g : priceList) {
            Map<String, Object> p = new HashMap<>();
            p.put("khungGioBatDau", g.getKhungGioBatDau() != null ? g.getKhungGioBatDau().format(timeFmt) : "");
            p.put("khungGioKetThuc", g.getKhungGioKetThuc() != null ? g.getKhungGioKetThuc().format(timeFmt) : "");
            p.put("giaThue", g.getGiaThue());
            priceData.add(p);
        }

        try {
            model.addAttribute("priceJson", new ObjectMapper().writeValueAsString(priceData));
        } catch (Exception e) {
            model.addAttribute("priceJson", "[]");
        }

        double depositRate = getDepositRate();
        model.addAttribute("depositRate", depositRate);
        model.addAttribute("depositPercentText", String.format("%.0f%%", depositRate * 100));
        model.addAttribute("pageTitle", "Đặt sân | UTEScore");
        return "customer/booking";
    }

    /* ===========================================================
     * 📩 [POST] Khi bấm “Đặt sân ngay” từ danh sách sân
     * =========================================================== */
    @PostMapping("/booking")
    public String redirectToBooking(@RequestParam("pitchId") Integer pitchId,
                                    HttpSession session) {
        if (SessionUtil.getCustomerEmail(session) == null)
            return "redirect:/login";
        return "redirect:/customer/booking?pitch=" + pitchId;
    }

    /* ===========================================================
     * ⏰ [GET] API: Lấy danh sách khung giờ đã được đặt của sân
     * =========================================================== */
    @GetMapping("/booked-slots")
    @ResponseBody
    public List<String> getBookedSlots(@RequestParam("pitchId") Integer pitchId,
                                       @RequestParam("date") String dateStr) {
        try {
            LocalDate date = LocalDate.parse(dateStr);
            LocalDateTime startOfDay = date.atStartOfDay();
            LocalDateTime endOfDay = date.atTime(LocalTime.MAX);
            List<ThueSan> bookings = thueSanRepository.findBySanBong_MaSanAndNgayThueBetween(pitchId, startOfDay, endOfDay);

            List<String> result = new ArrayList<>();
            LocalDateTime now = LocalDateTime.now();
            for (ThueSan t : bookings) {
                LocalDateTime endTime = t.getNgayThue().with(t.getKhungGioKetThuc());
                if (endTime.isBefore(now)) continue;

                boolean isPending = t.getGhiChu() != null && t.getGhiChu().contains("Chờ thanh toán VNPay")
                        && t.getHanGiuCho() != null && t.getHanGiuCho().isAfter(now);

                boolean isConfirmed = t.getThanhToans() != null && t.getThanhToans().stream().anyMatch(
                        tt -> {
                            String status = tt.getTrangThaiThanhToan();
                            return status != null && (status.equalsIgnoreCase("Đã thanh toán") || status.equalsIgnoreCase("Thành công"));
                        });

                if (isPending || isConfirmed) {
                    result.add(t.getKhungGioBatDau() + "-" + t.getKhungGioKetThuc());
                }
            }
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    /* ===========================================================
     * 💳 [POST] Xác nhận đặt sân & lưu DB trước khi thanh toán
     * =========================================================== */
    @PostMapping("/booking/confirm")
    public String confirmBooking(@RequestParam("pitchId") Integer pitchId,
                                 @RequestParam("amount") long cocAmount,
                                 @RequestParam("date") String date,
                                 @RequestParam("start") String start,
                                 @RequestParam("end") String end,
                                 @RequestParam("total") Long total,
                                 HttpSession session,
                                 Model model) {
        String email = SessionUtil.getCustomerEmail(session);
        if (email == null) return "redirect:/login";

        Optional<KhachHang> khOpt = khachHangRepository.findByEmail(email);
        Optional<SanBong> sanOpt = sanBongRepository.findById(pitchId);
        if (khOpt.isEmpty() || sanOpt.isEmpty()) return "redirect:/customer/home";

        KhachHang kh = khOpt.get();
        SanBong san = sanOpt.get();

        List<ThueSan> existing = thueSanRepository.findByKhachHang(kh);

     // 🔍 Chỉ kiểm tra những đơn còn hiệu lực trong 10 phút giữ chỗ
     boolean hasActiveHold = existing.stream().anyMatch(t -> {
         if (t.getGhiChu() == null || !t.getGhiChu().contains("Chờ thanh toán VNPay"))
             return false;

         LocalDateTime hanGiuCho = t.getHanGiuCho();
         // Nếu chưa có hạn giữ chỗ hoặc đã hết hạn thì không tính là giữ chỗ
         if (hanGiuCho == null || LocalDateTime.now().isAfter(hanGiuCho))
             return false;

         // Nếu còn trong thời gian 10 phút giữ chỗ
         return true;
     });

     if (hasActiveHold) {
         model.addAttribute("errorMessage",
             "⚠️ Bạn đang có một đơn đang chờ thanh toán cọc VNPay trong thời gian giữ chỗ. " +
             "Vui lòng hoàn tất thanh toán hoặc đợi hết 10 phút để đặt sân mới.");
         model.addAttribute("showAlert", true);
         return showBookingPage(pitchId, model, session);
     }


        try {
            ThueSan thueSan = new ThueSan();
            thueSan.setKhachHang(kh);
            thueSan.setSanBong(san);
            thueSan.setNgayTao(LocalDateTime.now());
            thueSan.setNgayThue(LocalDate.parse(date).atStartOfDay());
            thueSan.setKhungGioBatDau(LocalTime.parse(start));
            thueSan.setKhungGioKetThuc(LocalTime.parse(end));
            thueSan.setTongTien(BigDecimal.valueOf(total));
            thueSan.setTienCocBatBuoc(BigDecimal.valueOf(cocAmount));
            thueSan.setHanGiuCho(LocalDateTime.now().plusMinutes(10));
            thueSan.setGhiChu("Chờ thanh toán VNPay");
            thueSanRepository.save(thueSan);

            String orderInfo = kh.getSoDienThoai() + "_" + thueSan.getMaDonDat();
            return "redirect:/api/vnpay/create-payment?amount=" + cocAmount + "&orderInfo=" + orderInfo;
        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/customer/booking?pitch=" + pitchId + "&error=save-failed";
        }
    }

    /* ===========================================================
     * 💳 [GET] Thanh toán cọc lại qua VNPay cho đơn chưa thanh toán
     * =========================================================== */
    @GetMapping("/payment/deposit")
    public String payDeposit(@RequestParam("maDonDat") Integer maDonDat, HttpSession session) {
        String email = SessionUtil.getCustomerEmail(session);
        if (email == null) return "redirect:/login";

        Optional<ThueSan> opt = thueSanRepository.findById(maDonDat);
        if (opt.isEmpty()) return "redirect:/customer/history";

        ThueSan ts = opt.get();
        ts.setGhiChu("Chờ thanh toán VNPay");
        ts.setHanGiuCho(LocalDateTime.now().plusMinutes(10));
        thueSanRepository.save(ts);

        String orderInfo = ts.getKhachHang().getSoDienThoai() + "_" + ts.getMaDonDat();
        long amount = ts.getTienCocBatBuoc() != null ? ts.getTienCocBatBuoc().longValue() : 0;
        return "redirect:/api/vnpay/create-payment?amount=" + amount + "&orderInfo=" + orderInfo;
    }

    /* ===========================================================
     * ❌ [GET] Hủy đơn chưa thanh toán
     * =========================================================== */
    @GetMapping("/cancel-booking")
    public String cancelBooking(@RequestParam("maDonDat") Integer maDonDat,
                                HttpSession session) {
        String email = SessionUtil.getCustomerEmail(session);
        if (email == null) return "redirect:/login";

        Optional<ThueSan> opt = thueSanRepository.findById(maDonDat);
        if (opt.isPresent()) {
            ThueSan ts = opt.get();

            // ❗ Chỉ cho phép hủy khi chưa thanh toán
            boolean daThanhToan = ts.getThanhToans() != null && ts.getThanhToans().stream()
                    .anyMatch(t -> t.getTrangThaiThanhToan() != null &&
                            (t.getTrangThaiThanhToan().equalsIgnoreCase("Đã thanh toán") ||
                             t.getTrangThaiThanhToan().equalsIgnoreCase("Thành công")));

            if (!daThanhToan) {
                ts.setGhiChu("Khách hàng đã hủy đơn vào " + LocalDateTime.now().format(
                        DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")));
                ts.setHanGiuCho(null);
                thueSanRepository.save(ts);
            }
        }

        return "redirect:/customer/history";
    }


    /* ===========================================================
     * ⚙️ Lấy tỷ lệ cọc từ SystemConfigurations
     * =========================================================== */
    private double getDepositRate() {
        try {
            String val = systemConfigRepo.findValueByKey("depositRate");
            if (val != null && !val.isBlank()) {
                double parsed = Double.parseDouble(val.trim());
                return parsed > 1 ? parsed / 100 : parsed;
            }
        } catch (Exception e) {
            System.err.println("⚠️ Không thể đọc tỷ lệ cọc: " + e.getMessage());
        }
        return 0.3;
    }

    private String getPitchSize(String loaiSan) {
        if (loaiSan == null) return "";
        return switch (loaiSan.trim().toLowerCase()) {
            case "5 người" -> "20 × 40 m";
            case "7 người" -> "30 × 50 m";
            case "11 người" -> "105 × 68 m";
            default -> "";
        };
    }
}
