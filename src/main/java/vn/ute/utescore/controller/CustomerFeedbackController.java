package vn.ute.utescore.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import vn.ute.utescore.model.*;
import vn.ute.utescore.repository.*;
import vn.ute.utescore.utils.SessionUtil;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/customer")
public class CustomerFeedbackController {

    private final CustomerGopYHeThongRepository gopYRepo;
    private final CustomerKhachHangRepository khachRepo;
    private final CustomerSanBongRepository sanRepo;
    private final CustomerThueSanRepository thueSanRepo;
    private final CustomerThanhToanRepository thanhToanRepo;
    private final CustomerDanhGiaDonHangRepository danhGiaRepo;
    public CustomerFeedbackController(CustomerGopYHeThongRepository gopYRepo,
            CustomerKhachHangRepository khachRepo,
            CustomerSanBongRepository sanRepo,
            CustomerThueSanRepository thueSanRepo,
            CustomerThanhToanRepository thanhToanRepo,
            CustomerDanhGiaDonHangRepository danhGiaRepo) {
this.gopYRepo = gopYRepo;
this.khachRepo = khachRepo;
this.sanRepo = sanRepo;
this.thueSanRepo = thueSanRepo;
this.thanhToanRepo = thanhToanRepo;
this.danhGiaRepo = danhGiaRepo;
}


    /* ======================================================
       💬 TRANG GÓP Ý & ĐÁNH GIÁ
    ====================================================== */
    @GetMapping("/feedback")
    public String feedbackPage(Model model, HttpSession session) {
        String email = SessionUtil.getCustomerEmail(session);
        if (email == null) return "redirect:/login";
        model.addAttribute("pageTitle", "💬 Góp ý & Đánh giá | UTEScore");
        return "customer/feedback";
    }

    /* ======================================================
       ⭐ LOAD ĐƠN ĐỦ ĐIỀU KIỆN ĐÁNH GIÁ
       (Đã thanh toán phần “Còn lại”, Thành công, không hủy/hoàn)
    ====================================================== */
    @GetMapping("/feedback/eligible-orders")
    @ResponseBody
    public List<Map<String, Object>> getEligibleOrders(HttpSession session) {
        String email = SessionUtil.getCustomerEmail(session);
        if (email == null) return List.of();
        Optional<KhachHang> khOpt = khachRepo.findByEmail(email);
        if (khOpt.isEmpty()) return List.of();

        KhachHang kh = khOpt.get();
        List<ThueSan> donDatList = thueSanRepo.findByKhachHang(kh);

        // 🟢 Loại bỏ các đơn đã được đánh giá
        Set<Integer> daDanhGia = danhGiaRepo.findByKhachHang(kh)
                .stream()
                .map(d -> d.getThueSan().getMaDonDat())
                .collect(Collectors.toSet());

        return donDatList.stream()
                .filter(ts -> {
                    boolean daDa = ts.getNgayThue().isBefore(LocalDateTime.now());
                    boolean thanhToanThanhCong = thanhToanRepo.existsByThueSan_MaDonDatAndLoaiThanhToanAndTrangThaiThanhToan(
                            ts.getMaDonDat(), "Đã thanh toán", "Thành công"
                    );
                    boolean hopLe = ts.getGhiChu() == null ||
                            (!ts.getGhiChu().toLowerCase().contains("hủy")
                                    && !ts.getGhiChu().toLowerCase().contains("hoàn"));
                    return daDa && thanhToanThanhCong && hopLe && !daDanhGia.contains(ts.getMaDonDat());
                })
                .map(ts -> {
                    Map<String, Object> map = new LinkedHashMap<>();
                    map.put("maDonDat", ts.getMaDonDat());
                    map.put("tenSan", ts.getSanBong().getTenSan());
                    map.put("ngayDa", ts.getNgayThue());
                    return map;
                })
                .collect(Collectors.toList());
    }

    /* ======================================================
       💡 GÓP Ý HỆ THỐNG (THÊM + CẬP NHẬT + XÓA)
    ====================================================== */

    /** 💡 Gửi hoặc cập nhật góp ý */
    @PostMapping("/feedback/system")
    @ResponseBody
    public String saveFeedback(@RequestParam("chuDe") String chuDe,
                               @RequestParam("noiDung") String noiDung,
                               @RequestParam(value = "maSan", required = false) Integer maSan,
                               @RequestParam(value = "maGopY", required = false) Integer maGopY,
                               HttpSession session) {
        String email = SessionUtil.getCustomerEmail(session);
        if (email == null) return "NOT_LOGGED_IN";
        Optional<KhachHang> khOpt = khachRepo.findByEmail(email);
        if (khOpt.isEmpty()) return "NOT_FOUND";

        GopYHeThong g;
        if (maGopY != null && gopYRepo.findById(maGopY).isPresent()) {
            // 🟡 Cập nhật góp ý
            g = gopYRepo.findById(maGopY).get();
            g.setLoaiGopY(chuDe);
            g.setNoiDung(noiDung);
            if (maSan != null)
                sanRepo.findById(maSan).ifPresent(g::setSanBong);
            else
                g.setSanBong(null);
        } else {
            // 🟢 Thêm góp ý mới
            g = new GopYHeThong();
            g.setKhachHang(khOpt.get());
            g.setLoaiGopY(chuDe);
            g.setNoiDung(noiDung);
            g.setNgayGopY(LocalDateTime.now());
            g.setTrangThaiXuLy("Chưa xử lý");
            if (maSan != null)
                sanRepo.findById(maSan).ifPresent(g::setSanBong);
        }
        gopYRepo.save(g);
        return "SUCCESS";
    }

    /** 💡 Lịch sử góp ý */
    @GetMapping("/feedback/system-history")
    @ResponseBody
    public List<Map<String, Object>> getSystemFeedbackHistory(HttpSession session) {
        String email = SessionUtil.getCustomerEmail(session);
        if (email == null) return List.of();
        Optional<KhachHang> khOpt = khachRepo.findByEmail(email);
        if (khOpt.isEmpty()) return List.of();

        KhachHang kh = khOpt.get();
        List<GopYHeThong> list = gopYRepo.findByKhachHangOrderByNgayGopYDesc(kh);

        return list.stream().map(g -> {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("maGopY", g.getMaGopY());
            map.put("chuDe", g.getLoaiGopY());
            map.put("noiDung", g.getNoiDung());
            map.put("ngayGopY", g.getNgayGopY());
            if (g.getSanBong() != null) {
                map.put("maSan", g.getSanBong().getMaSan());
                map.put("tenSan", g.getSanBong().getTenSan());
            } else {
                map.put("maSan", null);
                map.put("tenSan", null);
            }
            return map;
        }).collect(Collectors.toList());
    }

    /** 💡 Xóa góp ý */
    @PostMapping("/feedback/delete")
    @ResponseBody
    public String deleteFeedback(@RequestParam("maGopY") Integer maGopY, HttpSession session) {
        String email = SessionUtil.getCustomerEmail(session);
        if (email == null) return "NOT_LOGGED_IN";
        Optional<GopYHeThong> gOpt = gopYRepo.findById(maGopY);
        if (gOpt.isEmpty()) return "NOT_FOUND";

        GopYHeThong g = gOpt.get();
        if (g.getKhachHang() == null || !email.equals(g.getKhachHang().getEmail()))
            return "FORBIDDEN";

        gopYRepo.delete(g);
        return "SUCCESS";
    }

    /* ======================================================
       ⭐ GỬI ĐÁNH GIÁ ĐƠN HÀNG + LỊCH SỬ
    ====================================================== */
    @PostMapping("/feedback/order")
    @ResponseBody
    public String addOrUpdateRating(@RequestParam("maDonDat") Integer maDonDat,
                                    @RequestParam("rating") Integer rating,
                                    @RequestParam("noiDung") String noiDung,
                                    @RequestParam(value = "maDanhGia", required = false) Integer maDanhGia,
                                    HttpSession session) {

        String email = SessionUtil.getCustomerEmail(session);
        if (email == null) return "NOT_LOGGED_IN";

        Optional<KhachHang> khOpt = khachRepo.findByEmail(email);
        if (khOpt.isEmpty()) return "NOT_FOUND";
        KhachHang kh = khOpt.get();

        Optional<ThueSan> thueSanOpt = thueSanRepo.findById(maDonDat);
        if (thueSanOpt.isEmpty()) return "INVALID_ORDER";
        ThueSan thueSan = thueSanOpt.get();

        // 🔹 Nếu có maDanhGia => đây là chỉnh sửa
        if (maDanhGia != null) {
            Optional<DanhGiaDonHang> dgOpt = danhGiaRepo.findById(maDanhGia);
            if (dgOpt.isEmpty()) return "INVALID_REVIEW";

            DanhGiaDonHang dg = dgOpt.get();
            if (!dg.getKhachHang().equals(kh)) return "FORBIDDEN";

            dg.setRating(rating);
            dg.setNoiDung(noiDung);
            dg.setNgayDanhGia(LocalDateTime.now());
            danhGiaRepo.save(dg);
            return "SUCCESS";
        }

        // 🔹 Nếu không có maDanhGia => đây là tạo mới
        boolean thanhToanThanhCong = thanhToanRepo.existsByThueSan_MaDonDatAndLoaiThanhToanAndTrangThaiThanhToan(
                thueSan.getMaDonDat(), "Đã thanh toán", "Thành công");
        boolean daDa = thueSan.getNgayThue().isBefore(LocalDateTime.now());
        if (!(thanhToanThanhCong && daDa)) return "NOT_ELIGIBLE";

        boolean daDanhGia = danhGiaRepo.existsByThueSanAndKhachHang(thueSan, kh);
        if (daDanhGia) return "ALREADY_RATED";

        DanhGiaDonHang dg = new DanhGiaDonHang();
        dg.setKhachHang(kh);
        dg.setThueSan(thueSan);
        dg.setRating(rating);
        dg.setNoiDung(noiDung);
        dg.setNgayDanhGia(LocalDateTime.now());
        dg.setTrangThaiPhanHoi("Chưa phản hồi");
        danhGiaRepo.save(dg);

        return "SUCCESS";
    }



    @GetMapping("/feedback/reviews")
    @ResponseBody
    public List<Map<String, Object>> getReviewHistory(HttpSession session) {
        String email = SessionUtil.getCustomerEmail(session);
        if (email == null) return List.of();
        Optional<KhachHang> khOpt = khachRepo.findByEmail(email);
        if (khOpt.isEmpty()) return List.of();

        List<DanhGiaDonHang> danhGias = danhGiaRepo.findByKhachHang(khOpt.get());
        return danhGias.stream().map(dg -> {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("maDanhGia", dg.getMaDanhGia());
            map.put("maDonDat", dg.getThueSan().getMaDonDat());
            map.put("tenSan", dg.getThueSan().getSanBong().getTenSan());
            map.put("rating", dg.getRating());
            map.put("noiDung", dg.getNoiDung());
            map.put("ngayDanhGia", dg.getNgayDanhGia());
            return map;
        }).collect(Collectors.toList());
    }

    /* ======================================================
       ⚽️ LẤY DANH SÁCH SÂN BÓNG (CHO COMBOBOX)
    ====================================================== */
    @GetMapping("/sanbong/all")
    @ResponseBody
    public List<Map<String, Object>> getAllSanBong() {
        return sanRepo.findAll().stream()
                .map(sb -> {
                    Map<String, Object> map = new LinkedHashMap<>();
                    map.put("maSan", sb.getMaSan());
                    map.put("tenSan", sb.getTenSan());
                    return map;
                })
                .collect(Collectors.toList());
    }

    /* ======================================================
       📝 CẬP NHẬT LẠI ĐÁNH GIÁ (nếu khách sửa lại)
    ====================================================== */
    @PostMapping("/feedback/order/update")
    @ResponseBody
    public String updateRating(
            @RequestParam(value = "maDanhGia", required = false) Integer maDanhGia,
            @RequestParam("rating") Integer rating,
            @RequestParam("noiDung") String noiDung,
            HttpSession session) {

        String email = SessionUtil.getCustomerEmail(session);
        if (email == null) return "NOT_LOGGED_IN";
        if (maDanhGia == null) return "MISSING_ID";

        var dgOpt = danhGiaRepo.findById(maDanhGia);
        if (dgOpt.isEmpty()) return "NOT_FOUND";

        var dg = dgOpt.get();
        if (dg.getKhachHang() == null || !email.equals(dg.getKhachHang().getEmail()))
            return "FORBIDDEN";

        dg.setRating(rating);
        dg.setNoiDung(noiDung);
        dg.setNgayDanhGia(LocalDateTime.now());
        danhGiaRepo.save(dg);
        return "SUCCESS";
    }
}
