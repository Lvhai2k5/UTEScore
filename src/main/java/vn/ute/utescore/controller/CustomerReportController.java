package vn.ute.utescore.controller;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import vn.ute.utescore.dto.CustomerThueSanViewDTO;
import vn.ute.utescore.model.*;
import vn.ute.utescore.repository.*;
import vn.ute.utescore.service.CustomerExcelReportService;
import vn.ute.utescore.utils.SessionUtil;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.Normalizer;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/customer")
public class CustomerReportController {

    private final CustomerThueSanRepository thueSanRepo;
    private final CustomerThanhToanRepository thanhToanRepo;
    private final CustomerHoanTienRepository hoanTienRepo;
    private final CustomerKhachHangRepository khachRepo;
    private final CustomerExcelReportService excelReportService;
    private final CustomerSanBongRepository sanBongRepo;
    
    public CustomerReportController(CustomerThueSanRepository thueSanRepo,
            CustomerThanhToanRepository thanhToanRepo,
            CustomerHoanTienRepository hoanTienRepo,
            CustomerKhachHangRepository khachRepo,
            CustomerExcelReportService excelReportService,
            CustomerSanBongRepository sanBongRepo) {
this.thueSanRepo = thueSanRepo;
this.thanhToanRepo = thanhToanRepo;
this.hoanTienRepo = hoanTienRepo;
this.khachRepo = khachRepo;
this.excelReportService = excelReportService;
this.sanBongRepo = sanBongRepo;
}

    /* =====================================================
     * 📜 LỊCH SỬ ĐẶT SÂN + BIỂU ĐỒ + PHÂN TRANG
     * ===================================================== */
    @GetMapping("/history")
    public String history(@RequestParam(defaultValue = "1") int page,
                          @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
                          @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate,
                          Model model, HttpSession session) {

        // 🧩 Lấy khách hàng đăng nhập
        String email = SessionUtil.getCustomerEmail(session);
        if (email == null) return "redirect:/login";
        KhachHang kh = khachRepo.findByEmail(email).orElse(null);
        if (kh == null) return "redirect:/login";

        // 🗓️ Xác định khoảng ngày
        LocalDate today = LocalDate.now();
        LocalDate f = (fromDate != null) ? fromDate : today.minusDays(29);
        LocalDate t = (toDate != null) ? toDate : today;
        LocalDateTime fromDT = f.atStartOfDay();
        LocalDateTime toDT = t.atTime(23, 59, 59);
        LocalDateTime now = LocalDateTime.now();

        // ✅ Phân trang danh sách đặt sân
        PageRequest pageable = PageRequest.of(page - 1, 5);
        Page<ThueSan> bookingsPage =
                thueSanRepo.findRecentBookings(kh, fromDT, toDT, pageable);

        // ✅ Xử lý danh sách hiển thị
        List<CustomerThueSanViewDTO> bookingsView = bookingsPage.getContent().stream().map(ts -> {

            String trangThai;
            long countdown = 0;
            LocalDateTime nowTime = LocalDateTime.now();
            LocalDateTime hanGiuCho = ts.getHanGiuCho();

            List<ThanhToan> pays = thanhToanRepo.findByThueSan(ts);

            if (pays.isEmpty()) {
                // ⏳ Chưa có thanh toán
                if (hanGiuCho != null && nowTime.isBefore(hanGiuCho)) {
                    countdown = Duration.between(nowTime, hanGiuCho).toSeconds();
                    trangThai = "⏳ Chưa thanh toán (đang giữ chỗ)";
                } else {
                    trangThai = "❌ Đã hủy (hết thời gian giữ chỗ)";
                }
            } else {
                // ✅ Kiểm tra loại thanh toán
                List<String> types = pays.stream()
                        .filter(tt -> "Thành công".equalsIgnoreCase(tt.getTrangThaiThanhToan())
                                   || "Chờ duyệt".equalsIgnoreCase(tt.getTrangThaiThanhToan()))
                        .map(tt -> tt.getLoaiThanhToan().trim())
                        .distinct()
                        .collect(Collectors.toList());

                boolean hasRefundPending = pays.stream()
                        .anyMatch(tt -> "Hoàn tiền".equalsIgnoreCase(tt.getLoaiThanhToan())
                                     && "Chờ duyệt".equalsIgnoreCase(tt.getTrangThaiThanhToan()));

                boolean hasRefundDone = pays.stream()
                        .anyMatch(tt -> "Hoàn tiền".equalsIgnoreCase(tt.getLoaiThanhToan())
                                     && "Thành công".equalsIgnoreCase(tt.getTrangThaiThanhToan()));

                // 🔽 Xác định trạng thái
                if (hasRefundPending) {
                    trangThai = "⏳ Đang chờ xử lý hoàn đơn";
                } else if (hasRefundDone) {
                    trangThai = "💸 Hoàn đơn (đã hoàn tiền)";
                } else if (types.contains("Hoàn tất")) {
                    trangThai = "✅ Hoàn tất (đã check-in)";
                } else if (types.contains("Đã thanh toán")) {
                    trangThai = "💰 Đã thanh toán (đủ tiền)";
                } else if (types.contains("Đặt cọc")) {
                    LocalDateTime endTime = null;
                    if (ts.getNgayThue() != null && ts.getKhungGioKetThuc() != null) {
                        LocalTime end = ts.getKhungGioKetThuc();
                        endTime = ts.getNgayThue()
                                .withHour(end.getHour())
                                .withMinute(end.getMinute())
                                .withSecond(0);
                    }

                    if (endTime != null && LocalDateTime.now().isAfter(endTime.plusMinutes(10))) {
                        trangThai = "💵 Đã đặt cọc — khách không đến";
                    } else {
                        trangThai = "💵 Đã đặt cọc (30%)";
                    }
                } else {
                    trangThai = "⏳ Chưa thanh toán";
                }
            }

            return new CustomerThueSanViewDTO(ts, trangThai, countdown);
        }).toList();

        // 📦 Gửi dữ liệu sang View
        model.addAttribute("bookings", bookingsPage);
        model.addAttribute("bookingsView", bookingsView);
        model.addAttribute("fromDate", f);
        model.addAttribute("toDate", t);
        model.addAttribute("pageTitle", "📜 Lịch sử đặt sân");

        /* =====================================================
         * 📊 BIỂU ĐỒ THỐNG KÊ
         * ===================================================== */
        List<ThueSan> all = thueSanRepo.findByKhachHangAndNgayThueBetween(kh, fromDT, toDT);

        Map<String, Long> successAgg = new LinkedHashMap<>();
        Map<String, Long> cancelAgg = new LinkedHashMap<>();
        Map<String, Long> refundAgg = new LinkedHashMap<>();

        YearMonth startYM = YearMonth.from(f);
        YearMonth endYM = YearMonth.from(t);
        for (YearMonth ym = startYM; !ym.isAfter(endYM); ym = ym.plusMonths(1)) {
            successAgg.put(ym.toString(), 0L);
            cancelAgg.put(ym.toString(), 0L);
            refundAgg.put(ym.toString(), 0L);
        }

        // ✅ Duyệt từng đơn để phân loại đúng nhóm biểu đồ
        for (ThueSan ts : all) {
            String key = YearMonth.from(ts.getNgayThue()).toString();
            List<ThanhToan> pays = thanhToanRepo.findByThueSan(ts);
            List<String> types = pays.stream()
                    .filter(tt -> "Thành công".equalsIgnoreCase(tt.getTrangThaiThanhToan()))
                    .map(tt -> tt.getLoaiThanhToan().trim())
                    .distinct()
                    .collect(Collectors.toList());

            if (types.contains("Hoàn tiền")) {
                refundAgg.computeIfPresent(key, (k, v) -> v + 1);
            } else if (types.contains("Đã thanh toán") || types.contains("Hoàn tất")) {
                successAgg.computeIfPresent(key, (k, v) -> v + 1);
            } else if (types.isEmpty()) {
                // Hủy do không thanh toán cọc
                if (ts.getHanGiuCho() != null && now.isAfter(ts.getHanGiuCho().plusMinutes(10))) {
                    cancelAgg.computeIfPresent(key, (k, v) -> v + 1);
                }
            }
        }

     // 💰 Doanh thu 3 tháng gần nhất
        YearMonth thisMonth = YearMonth.from(today);
        List<YearMonth> last3 = List.of(
                thisMonth.minusMonths(2),
                thisMonth.minusMonths(1),
                thisMonth
        );

        // ✅ Khởi tạo map 3 tháng gần nhất
        Map<String, BigDecimal> last3Agg = new LinkedHashMap<>();
        last3.forEach(m -> last3Agg.put(m.toString(), BigDecimal.ZERO));

        // ✅ Lấy tất cả giao dịch THÀNH CÔNG trong 3 tháng gần nhất
        List<ThanhToan> paysLast3 = thanhToanRepo
                .findByThueSan_KhachHangAndTrangThaiThanhToanAndNgayThanhToanBetween(
                        kh, "Thành công",
                        last3.get(0).atDay(1).atStartOfDay(),
                        last3.get(2).atEndOfMonth().atTime(23, 59, 59)
                );

        // ✅ Cộng/trừ doanh thu theo loại thanh toán
        for (ThanhToan p : paysLast3) {
            if (p.getNgayThanhToan() == null) continue;
            YearMonth ym = YearMonth.from(p.getNgayThanhToan());
            if (!last3Agg.containsKey(ym.toString())) continue;

            // Lấy giá trị thanh toán (nếu null thì 0)
            BigDecimal val = Optional.ofNullable(p.getSoTienNhan()).orElse(BigDecimal.ZERO);
            String loai = Optional.ofNullable(p.getLoaiThanhToan()).orElse("").trim().toLowerCase();

            // ✅ Cộng loại “đặt cọc” và “đã thanh toán”
            if (loai.contains("đặt cọc") || loai.contains("đã thanh toán")) {
                last3Agg.put(ym.toString(), last3Agg.get(ym.toString()).add(val));
            }

            // ❌ Trừ loại “hoàn đơn”
            else if (loai.contains("hoàn")) {
                last3Agg.put(ym.toString(), last3Agg.get(ym.toString()).subtract(val));
            }

            // ⚠️ Bỏ qua loại khác
        }

        // ✅ Đưa dữ liệu ra cho biểu đồ
        model.addAttribute("labelsStatus", new ArrayList<>(successAgg.keySet()));
        model.addAttribute("successData", new ArrayList<>(successAgg.values()));
        model.addAttribute("cancelData", new ArrayList<>(cancelAgg.values()));
        model.addAttribute("refundData", new ArrayList<>(refundAgg.values()));
        model.addAttribute("labels3", new ArrayList<>(last3Agg.keySet()));
        model.addAttribute("data3", last3Agg.values().stream()
                .map(v -> v.longValue() < 0 ? 0 : v.longValue()) // không âm
                .toList());

        return "customer/history";

    }

    /* =====================================================
     * 📊 XUẤT FILE EXCEL
     * ===================================================== */
    @GetMapping("/export-history")
    public void exportCustomerHistory(HttpServletResponse response, HttpSession session) throws IOException {
        String email = SessionUtil.getCustomerEmail(session);
        if (email == null) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        KhachHang kh = khachRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy khách hàng: " + email));

        String customerName = kh.getHoTen();
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime fromDate = now.minusMonths(3);
        LocalDateTime toDate = now;

        excelReportService.exportReport(response, customerName, fromDate, toDate);

        String safeName = removeVietnameseAccents(customerName).replaceAll("\\s+", "_");
        String fileName = String.format("ChiTieu_%s_thang%d_thang%d_%s.xlsx",
                safeName, fromDate.getMonthValue(), toDate.getMonthValue(),
                now.format(DateTimeFormatter.ofPattern("ddMMyyyy")));

        String encoded = URLEncoder.encode(fileName, StandardCharsets.UTF_8).replace("+", "%20");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename*=UTF-8''" + encoded);
    }

    // 🔠 Loại bỏ dấu tiếng Việt để xuất tên file Excel
    private static String removeVietnameseAccents(String input) {
        if (input == null) return "";
        String temp = Normalizer.normalize(input, Normalizer.Form.NFD);
        return temp.replaceAll("\\p{M}", "")
                .replaceAll("Đ", "D")
                .replaceAll("đ", "d");
    }
    @GetMapping("/api/invoice")
    @ResponseBody
    public Map<String, Object> getInvoiceData(@RequestParam("maDonDat") Integer maDonDat) {
        Map<String, Object> map = new LinkedHashMap<>();

        ThueSan ts = thueSanRepo.findById(maDonDat).orElse(null);
        if (ts == null) return Map.of("error", "Không tìm thấy đơn #" + maDonDat);

        List<ThanhToan> thanhToans = thanhToanRepo.findByThueSan(ts);

        DateTimeFormatter fmtDate = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter fmtDateTime = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

        map.put("maDonDat", ts.getMaDonDat());
        map.put("khachHang", ts.getKhachHang() != null ? ts.getKhachHang().getHoTen() : "Không xác định");
        map.put("sanBong", ts.getSanBong() != null ? ts.getSanBong().getTenSan() : "Không xác định");
        map.put("ngayThue", ts.getNgayThue() != null ? ts.getNgayThue().format(fmtDate) : null);
        map.put("khungGio", (ts.getKhungGioBatDau() != null ? ts.getKhungGioBatDau().toString() : "?") +
                " - " + (ts.getKhungGioKetThuc() != null ? ts.getKhungGioKetThuc().toString() : "?"));
        map.put("tongTien", ts.getTongTien() != null ? ts.getTongTien() : BigDecimal.ZERO);

        List<Map<String, Object>> ttList = thanhToans.stream().map(tt -> {
            Map<String, Object> item = new LinkedHashMap<>();
            item.put("loai", tt.getLoaiThanhToan() != null ? tt.getLoaiThanhToan() : "Không rõ");
            item.put("pttt", tt.getPhuongThuc() != null ? tt.getPhuongThuc() : "Không rõ");
            item.put("soTien", tt.getSoTienNhan() != null ? tt.getSoTienNhan() : BigDecimal.ZERO);
            item.put("trangThai", tt.getTrangThaiThanhToan() != null ? tt.getTrangThaiThanhToan() : "Không rõ");
            item.put("ngay", tt.getNgayThanhToan() != null ? tt.getNgayThanhToan().format(fmtDateTime) : null);
            return item;
        }).collect(Collectors.toList());

        map.put("thanhToans", ttList);
        return map;
    }
    @PostMapping("/refund-request")
    @ResponseBody
    @Transactional
    public Map<String, Object> createRefundRequest(
            @RequestParam("maDonDat") Integer maDonDat,
            @RequestBody Map<String, Object> body
    ) {
        try {
            // 🔹 1. Kiểm tra đơn thuê sân tồn tại
            ThueSan ts = thueSanRepo.findById(maDonDat).orElse(null);
            if (ts == null)
                return Map.of("success", false, "message", "Không tìm thấy đơn thuê sân #" + maDonDat);

            // 🔹 2. Chặn nếu đã yêu cầu hoàn trước đó
            if (ts.getGhiChu() != null && ts.getGhiChu().toLowerCase().contains("hoàn"))
                return Map.of("success", false, "message", "Đơn này đã được hoàn hoặc đang chờ xử lý!");

            // 🔹 3. Lấy giao dịch ĐẶT CỌC thành công
            ThanhToan depositPay = thanhToanRepo.findByThueSanAndLoaiThanhToanAndTrangThaiThanhToan(
                    ts, "Đặt cọc", "Thành công"
            ).stream().findFirst().orElse(null);

            if (depositPay == null)
                return Map.of("success", false, "message", "Không tìm thấy giao dịch đặt cọc hợp lệ để hoàn tiền.");

            // ✅ Lấy số tiền đặt cọc
            BigDecimal soTienHoan = depositPay.getSoTienNhan();

            // 🔹 4. Tạo bản ghi ThanhToan loại "Hoàn tiền"
            ThanhToan refundTrans = new ThanhToan();
            refundTrans.setThueSan(ts);
            refundTrans.setLoaiThanhToan("Hoàn tiền");
            refundTrans.setTrangThaiThanhToan("Chờ duyệt");
            refundTrans.setPhuongThuc("Chuyển khoản");
            refundTrans.setSoTienNhan(soTienHoan); // 💰 Hoàn đúng số tiền đã đặt cọc
            refundTrans.setNgayThanhToan(LocalDateTime.now());
            refundTrans.setGhiChu("Hoàn lại tiền đặt cọc #" + depositPay.getMaThanhToan() +
                    " của đơn #" + ts.getMaDonDat());
            thanhToanRepo.save(refundTrans);

            // 🔹 5. Tạo bản ghi HoanTien
            HoanTien hoanTien = new HoanTien();
            hoanTien.setThanhToan(refundTrans);
            hoanTien.setSoTienHoan(soTienHoan); // 💰 Hoàn cùng số tiền đặt cọc
            hoanTien.setPhuongThucHoan("Chuyển khoản");
            hoanTien.setNganHang((String) body.getOrDefault("nganHang", "Không rõ"));
            hoanTien.setSoTaiKhoan((String) body.getOrDefault("soTaiKhoan", "Không rõ"));
            hoanTien.setLyDoHoan((String) body.getOrDefault("lyDoHoan", ""));
            hoanTien.setNgayYeuCau(LocalDateTime.now());
            hoanTien.setTrangThaiHoan("Chờ duyệt");
            hoanTienRepo.save(hoanTien);

            // 🔹 6. Cập nhật đơn thuê sân
            ts.setGhiChu("Đã yêu cầu hoàn đơn (Ngày: " + LocalDateTime.now() + ")");
            ts.setHanGiuCho(null);
            thueSanRepo.save(ts);

            return Map.of(
                    "success", true,
                    "message", "Yêu cầu hoàn tiền " + soTienHoan + " VNĐ đã được ghi nhận. Sân đã được trả lại, vui lòng chờ xác nhận."
            );

        } catch (Exception e) {
            e.printStackTrace();
            return Map.of("success", false, "message", "Lỗi khi xử lý hoàn đơn: " + e.getMessage());
        }
    }

}
