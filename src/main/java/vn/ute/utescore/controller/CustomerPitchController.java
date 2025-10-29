package vn.ute.utescore.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import vn.ute.utescore.dto.CustommerSanBongView;
import vn.ute.utescore.model.DanhGiaDonHang;
import vn.ute.utescore.model.GiaThue;
import vn.ute.utescore.model.SanBong;
import vn.ute.utescore.model.TinhNang;
import vn.ute.utescore.repository.CustomerDanhGiaDonHangRepository;
import vn.ute.utescore.repository.CustomerGiaThueRepository;
import vn.ute.utescore.repository.CustomerSanBongRepository;
import vn.ute.utescore.repository.CustomerTinhNangSanRepository;
import vn.ute.utescore.service.CustomerSanBongService;
import vn.ute.utescore.service.CustomerTinhNangService;

import java.math.BigDecimal;
import java.text.Normalizer;
import java.text.NumberFormat;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.Locale;

@Controller
@RequestMapping("/customer")
public class CustomerPitchController {

    private final CustomerSanBongService customerSanBongService;
    private final CustomerGiaThueRepository giaThueRepository;
    private final CustomerTinhNangService tinhNangService;
    private final CustomerSanBongRepository sanBongRepository;
    private final CustomerTinhNangSanRepository tinhNangSanRepository;
    private final CustomerDanhGiaDonHangRepository danhGiaDonHangRepository;

    public CustomerPitchController(CustomerSanBongService customerSanBongService,
            CustomerGiaThueRepository giaThueRepository,
            CustomerTinhNangService tinhNangService,
            CustomerSanBongRepository sanBongRepository,
            CustomerTinhNangSanRepository tinhNangSanRepository,
            CustomerDanhGiaDonHangRepository danhGiaDonHangRepository) {
this.customerSanBongService = customerSanBongService;
this.giaThueRepository = giaThueRepository;
this.tinhNangService = tinhNangService;
this.sanBongRepository = sanBongRepository;
this.tinhNangSanRepository = tinhNangSanRepository;
this.danhGiaDonHangRepository = danhGiaDonHangRepository;
}

    
    private static final String DEFAULT_PITCH_IMG = "/img/default-pitch.jpg";

    // 1) Danh sách sân - GET
    @GetMapping("/pitch-list")
    public String listPitch(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String loaiSan,
            @RequestParam(required = false) String trangThai,
            @RequestParam(required = false) Integer maTinhNang,
            @RequestParam(required = false) String gioBatDau,
            @RequestParam(required = false) String gioKetThuc,
            Model model
    ) {
        loadPitchList(keyword, loaiSan, trangThai, maTinhNang, gioBatDau, gioKetThuc, model);
        return "customer/pitch-list";
    }

    // 2) Danh sách sân - POST (ẩn query string)
    @PostMapping("/pitch-list")
    public String filterPitchList(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String loaiSan,
            @RequestParam(required = false) String trangThai,
            @RequestParam(required = false) Integer maTinhNang,
            @RequestParam(required = false) String gioBatDau,
            @RequestParam(required = false) String gioKetThuc,
            Model model
    ) {
        loadPitchList(keyword, loaiSan, trangThai, maTinhNang, gioBatDau, gioKetThuc, model);
        return "customer/pitch-list";
    }

    // 3) Chi tiết sân
    @GetMapping("/pitch-detail/{id}")
    public String showPitchDetail(@PathVariable("id") Integer id, Model model) {
        Optional<SanBong> optionalSan = sanBongRepository.findById(id);
        if (optionalSan.isEmpty()) {
            model.addAttribute("pageTitle", "Lỗi 404 - Không tìm thấy sân bóng");
            model.addAttribute("message", "Không tìm thấy sân bóng có mã: " + id);
            return "error/404";
        }

        SanBong san = optionalSan.get();

        String img = safeImageUrl(san.getHinhAnh()); // URL ảnh String

        List<String> tinhNangList = tinhNangSanRepository.findTenTinhNangByMaSan(san.getMaSan());
        List<GiaThue> bangGia = giaThueRepository.findActiveByLoaiSan(san.getLoaiSan(), "Đang áp dụng");

        List<DanhGiaDonHang> danhGiaList = danhGiaDonHangRepository.findByMaSan(san.getMaSan());
        double avgRating = danhGiaList.isEmpty()
                ? 0
                : danhGiaList.stream().mapToInt(DanhGiaDonHang::getRating).average().orElse(0);

        model.addAttribute("san", san);
        model.addAttribute("img", img);
        model.addAttribute("tinhNangList", tinhNangList);
        model.addAttribute("bangGia", bangGia);
        model.addAttribute("danhGiaList", danhGiaList);
        model.addAttribute("avgRating", avgRating);
        model.addAttribute("pageTitle", "Chi tiết sân - " + san.getTenSan());

        return "customer/pitch-detail";
    }

    // Dùng chung cho GET/POST
    private void loadPitchList(
            String keyword, String loaiSan, String trangThai, Integer maTinhNang,
            String gioBatDau, String gioKetThuc, Model model
    ) {
        String kw = normalizeOrNull(keyword);
        String loai = normalizeOrNull(blankToNull(loaiSan));
        String tt   = normalizeOrNull(blankToNull(trangThai));

        List<SanBong> sanFiltered = customerSanBongService.timSanTheoDieuKien(
                kw, loai, tt, maTinhNang, gioBatDau, gioKetThuc
        );

        List<GiaThue> giaList = giaThueRepository.findByTrangThai("Đang áp dụng");
        List<TinhNang> tinhNangList = tinhNangService.findAll();

        List<CustommerSanBongView> pitchViews = convertToView(sanFiltered, giaList);

        model.addAttribute("pitchList", pitchViews);
        model.addAttribute("dsTinhNang", tinhNangList);
        model.addAttribute("keyword", keyword);
        model.addAttribute("loaiSan", loaiSan);
        model.addAttribute("trangThai", trangThai);
        model.addAttribute("maTinhNang", maTinhNang);
        model.addAttribute("gioBatDau", gioBatDau);
        model.addAttribute("gioKetThuc", gioKetThuc);
        model.addAttribute("pageTitle", "⚽ Danh sách sân bóng | UTEScore");
    }

    // Entity -> View DTO
    private List<CustommerSanBongView> convertToView(List<SanBong> sanList, List<GiaThue> giaList) {
        Map<String, List<GiaThue>> giaTheoLoai = giaList.stream()
                .filter(g -> g.getLoaiSan() != null)
                .collect(Collectors.groupingBy(g -> g.getLoaiSan().trim().toLowerCase()));

        List<CustommerSanBongView> list = new ArrayList<>();

        for (SanBong s : sanList) {
            String img = safeImageUrl(s.getHinhAnh());

            List<GiaThue> bangGia = giaTheoLoai.getOrDefault(
                    s.getLoaiSan() == null ? "" : s.getLoaiSan().trim().toLowerCase(),
                    new ArrayList<>()
            );

            BigDecimal giaMacDinh = bangGia.isEmpty()
                    ? BigDecimal.ZERO
                    : Optional.ofNullable(bangGia.get(0).getGiaThue()).orElse(BigDecimal.ZERO);

            StringBuilder giaTheoKhung = new StringBuilder();
            if (!bangGia.isEmpty()) {
                for (GiaThue g : bangGia) {
                	String tu = Optional.ofNullable(g.getKhungGioBatDau())
                	        .map(LocalTime::toString)
                	        .orElse("");
                	String den = Optional.ofNullable(g.getKhungGioKetThuc())
                	        .map(LocalTime::toString)
                	        .orElse("");
                    String giaVnd = formatVND(g.getGiaThue());
                    giaTheoKhung.append(tu).append(" - ").append(den)
                            .append(": ").append(giaVnd).append(" / giờ<br>");
                }
            } else {
                giaTheoKhung.append("<span style='color:gray'>Chưa cập nhật giá</span>");
            }

            List<String> tinhNangSan = new ArrayList<>();
            if (s.getTinhNangSans() != null) {
                s.getTinhNangSans().forEach(tns -> {
                    if (tns.getTinhNang() != null) {
                        tinhNangSan.add(tns.getTinhNang().getTenTinhNang());
                    }
                });
            }

            List<DanhGiaDonHang> danhGiaList = danhGiaDonHangRepository.findByMaSan(s.getMaSan());
            double rating = danhGiaList.isEmpty()
                    ? 0
                    : danhGiaList.stream().mapToInt(DanhGiaDonHang::getRating).average().orElse(0);

            list.add(CustommerSanBongView.builder()
                    .id(s.getMaSan())
                    .name(s.getTenSan())
                    .type(s.getLoaiSan())
                    .area(s.getKhuVuc())
                    .imageUrl(img)
                    .price(giaMacDinh)
                    .priceFormatted(giaTheoKhung.toString())
                    .active("Hoạt động".equalsIgnoreCase(s.getTrangThai()))
                    .features(tinhNangSan)
                    .rating(rating)
                    .totalReviews(danhGiaList.size())
                    .build());
        }

        return list;
    }

    // Helpers
    private String safeImageUrl(String url) {
        if (url == null || url.isBlank()) return DEFAULT_PITCH_IMG;
        // Muốn chặt hơn có thể chỉ chấp nhận http/https:
        // if (!url.startsWith("http")) return DEFAULT_PITCH_IMG;
        return url;
    }

    private String formatVND(BigDecimal amount) {
        if (amount == null) return "0đ";
        NumberFormat nf = NumberFormat.getInstance(new Locale("vi", "VN"));
        return nf.format(amount) + "đ";
    }

    private String normalizeOrNull(String s) {
        if (s == null) return null;
        String t = s.trim();
        return t.isEmpty() ? null : Normalizer.normalize(t, Normalizer.Form.NFC);
    }

    private String blankToNull(String s) {
        if (s == null) return null;
        String t = s.trim();
        return t.isEmpty() ? null : t;
    }
}


//package vn.ute.utescore.controller;
//
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.*;
//import vn.ute.utescore.dto.CustommerSanBongView;
//import vn.ute.utescore.model.DanhGiaDonHang;
//import vn.ute.utescore.model.GiaThue;
//import vn.ute.utescore.model.SanBong;
//import vn.ute.utescore.model.TinhNang;
//import vn.ute.utescore.repository.CustomerDanhGiaDonHangRepository;
//import vn.ute.utescore.repository.CustomerGiaThueRepository;
//import vn.ute.utescore.repository.CustomerSanBongRepository;
//import vn.ute.utescore.repository.CustomerTinhNangSanRepository;
//import vn.ute.utescore.service.CustomerSanBongService;
//import vn.ute.utescore.service.CustomerTinhNangService;
//
//import java.math.BigDecimal;
//import java.text.Normalizer;
//import java.text.NumberFormat;
//import java.time.LocalTime;
//import java.util.*;
//import java.util.stream.Collectors;
//import java.util.Locale;
//
//@Controller
//@RequestMapping("/customer")
//public class CustomerPitchController {
//
//    private final CustomerSanBongService customerSanBongService;
//    private final CustomerGiaThueRepository giaThueRepository;
//    private final CustomerTinhNangService tinhNangService;
//    private final CustomerSanBongRepository sanBongRepository;
//    private final CustomerTinhNangSanRepository tinhNangSanRepository;
//    private final CustomerDanhGiaDonHangRepository danhGiaDonHangRepository;
//
//    private static final String DEFAULT_PITCH_IMG = "/img/default-pitch.jpg";
//
//    public CustomerPitchController(CustomerSanBongService customerSanBongService,
//                                   CustomerGiaThueRepository giaThueRepository,
//                                   CustomerTinhNangService tinhNangService,
//                                   CustomerSanBongRepository sanBongRepository,
//                                   CustomerTinhNangSanRepository tinhNangSanRepository,
//                                   CustomerDanhGiaDonHangRepository danhGiaDonHangRepository) {
//        this.customerSanBongService = customerSanBongService;
//        this.giaThueRepository = giaThueRepository;
//        this.tinhNangService = tinhNangService;
//        this.sanBongRepository = sanBongRepository;
//        this.tinhNangSanRepository = tinhNangSanRepository;
//        this.danhGiaDonHangRepository = danhGiaDonHangRepository;
//    }
//
//    // ===================== DANH SÁCH SÂN =====================
//
//    @GetMapping("/pitch-list")
//    public String listPitch(
//            @RequestParam(required = false) String keyword,
//            @RequestParam(required = false) String loaiSan,
//            @RequestParam(required = false) String trangThai,
//            @RequestParam(required = false) Integer maTinhNang,
//            @RequestParam(required = false) String gioBatDau,
//            @RequestParam(required = false) String gioKetThuc,
//            Model model
//    ) {
//        loadPitchList(keyword, loaiSan, trangThai, maTinhNang, gioBatDau, gioKetThuc, model);
//        return "customer/pitch-list";
//    }
//
//    @PostMapping("/pitch-list")
//    public String filterPitchList(
//            @RequestParam(required = false) String keyword,
//            @RequestParam(required = false) String loaiSan,
//            @RequestParam(required = false) String trangThai,
//            @RequestParam(required = false) Integer maTinhNang,
//            @RequestParam(required = false) String gioBatDau,
//            @RequestParam(required = false) String gioKetThuc,
//            Model model
//    ) {
//        loadPitchList(keyword, loaiSan, trangThai, maTinhNang, gioBatDau, gioKetThuc, model);
//        return "customer/pitch-list";
//    }
//
//    // ===================== CHI TIẾT SÂN =====================
//
//    @GetMapping("/pitch-detail/{id}")
//    public String showPitchDetail(@PathVariable("id") Integer id, Model model) {
//        Optional<SanBong> optionalSan = sanBongRepository.findById(id);
//        if (optionalSan.isEmpty()) {
//            model.addAttribute("pageTitle", "Lỗi 404 - Không tìm thấy sân bóng");
//            model.addAttribute("message", "Không tìm thấy sân bóng có mã: " + id);
//            return "error/404";
//        }
//
//        SanBong san = optionalSan.get();
//        String img = safeImageUrl(san.getHinhAnh());
//
//        List<String> tinhNangList = tinhNangSanRepository.findTenTinhNangByMaSan(san.getMaSan());
//        List<GiaThue> bangGia = giaThueRepository.findActiveByLoaiSan(san.getLoaiSan(), "Đang áp dụng");
//
//        List<DanhGiaDonHang> danhGiaList = danhGiaDonHangRepository.findByMaSan(san.getMaSan());
//        double avgRating = danhGiaList.isEmpty()
//                ? 0
//                : danhGiaList.stream().mapToInt(DanhGiaDonHang::getRating).average().orElse(0);
//
//        model.addAttribute("san", san);
//        model.addAttribute("img", img);
//        model.addAttribute("tinhNangList", tinhNangList);
//        model.addAttribute("bangGia", bangGia);
//        model.addAttribute("danhGiaList", danhGiaList);
//        model.addAttribute("avgRating", avgRating);
//        model.addAttribute("pageTitle", "Chi tiết sân - " + san.getTenSan());
//
//        return "customer/pitch-detail";
//    }
//
//    // ===================== LOAD DANH SÁCH =====================
//
//    private void loadPitchList(
//            String keyword, String loaiSan, String trangThai, Integer maTinhNang,
//            String gioBatDau, String gioKetThuc, Model model
//    ) {
//        String kw = normalizeOrNull(keyword);
//        String loai = normalizeOrNull(blankToNull(loaiSan));
//        String tt = normalizeOrNull(blankToNull(trangThai));
//
//        List<SanBong> sanFiltered = customerSanBongService.timSanTheoDieuKien(
//                kw, loai, tt, maTinhNang, gioBatDau, gioKetThuc
//        );
//
//        List<GiaThue> giaList = giaThueRepository.findByTrangThai("Đang áp dụng");
//        List<TinhNang> tinhNangList = tinhNangService.findAll();
//
//        List<CustommerSanBongView> pitchViews = convertToView(sanFiltered, giaList);
//
//        model.addAttribute("pitchList", pitchViews);
//        model.addAttribute("dsTinhNang", tinhNangList);
//        model.addAttribute("keyword", keyword);
//        model.addAttribute("loaiSan", loaiSan);
//        model.addAttribute("trangThai", trangThai);
//        model.addAttribute("maTinhNang", maTinhNang);
//        model.addAttribute("gioBatDau", gioBatDau);
//        model.addAttribute("gioKetThuc", gioKetThuc);
//        model.addAttribute("pageTitle", "⚽ Danh sách sân bóng | UTEScore");
//    }
//
//    // ===================== CONVERT ENTITY -> VIEW DTO =====================
//
//    private List<CustommerSanBongView> convertToView(List<SanBong> sanList, List<GiaThue> giaList) {
//        Map<String, List<GiaThue>> giaTheoLoai = giaList.stream()
//                .filter(g -> g.getLoaiSan() != null)
//                .collect(Collectors.groupingBy(g -> g.getLoaiSan().trim().toLowerCase()));
//
//        List<CustommerSanBongView> list = new ArrayList<>();
//
//        for (SanBong s : sanList) {
//            String img = safeImageUrl(s.getHinhAnh());
//
//            List<GiaThue> bangGia = giaTheoLoai.getOrDefault(
//                    s.getLoaiSan() == null ? "" : s.getLoaiSan().trim().toLowerCase(),
//                    new ArrayList<>()
//            );
//
//            BigDecimal giaMacDinh = bangGia.isEmpty()
//                    ? BigDecimal.ZERO
//                    : Optional.ofNullable(bangGia.get(0).getGiaThue()).orElse(BigDecimal.ZERO);
//
//            StringBuilder giaTheoKhung = new StringBuilder();
//            if (!bangGia.isEmpty()) {
//                for (GiaThue g : bangGia) {
//                    String tu = Optional.ofNullable(g.getKhungGioBatDau())
//                            .map(LocalTime::toString)
//                            .orElse("");
//                    String den = Optional.ofNullable(g.getKhungGioKetThuc())
//                            .map(LocalTime::toString)
//                            .orElse("");
//                    String giaVnd = formatVND(g.getGiaThue());
//                    giaTheoKhung.append(tu).append(" - ").append(den)
//                            .append(": ").append(giaVnd).append(" / giờ<br>");
//                }
//            } else {
//                giaTheoKhung.append("<span style='color:gray'>Chưa cập nhật giá</span>");
//            }
//
//            List<String> tinhNangSan = new ArrayList<>();
//            if (s.getTinhNangSans() != null) {
//                s.getTinhNangSans().forEach(tns -> {
//                    if (tns.getTinhNang() != null) {
//                        tinhNangSan.add(tns.getTinhNang().getTenTinhNang());
//                    }
//                });
//            }
//
//            List<DanhGiaDonHang> danhGiaList = danhGiaDonHangRepository.findByMaSan(s.getMaSan());
//            double rating = danhGiaList.isEmpty()
//                    ? 0
//                    : danhGiaList.stream().mapToInt(DanhGiaDonHang::getRating).average().orElse(0);
//
//            list.add(CustommerSanBongView.builder()
//                    .id(s.getMaSan())
//                    .name(s.getTenSan())
//                    .type(s.getLoaiSan())
//                    .area(s.getKhuVuc())
//                    .imageUrl(img)
//                    .price(giaMacDinh)
//                    .priceFormatted(giaTheoKhung.toString())
//                    .active("Hoạt động".equalsIgnoreCase(s.getTrangThai()))
//                    .features(tinhNangSan)
//                    .rating(rating)
//                    .totalReviews(danhGiaList.size())
//                    .build());
//        }
//
//        return list;
//    }
//
//    // ===================== HELPER FUNCTIONS =====================
//
//    private String safeImageUrl(String url) {
//        if (url == null || url.isBlank()) return DEFAULT_PITCH_IMG;
//        if (url.startsWith("http")) return url; // Cloudinary hoặc URL online
//        return "/img/" + url; // ảnh cục bộ trong static/img
//    }
//
//    private String formatVND(BigDecimal amount) {
//        if (amount == null) return "0đ";
//        NumberFormat nf = NumberFormat.getInstance(new Locale("vi", "VN"));
//        return nf.format(amount) + "đ";
//    }
//
//    private String normalizeOrNull(String s) {
//        if (s == null) return null;
//        String t = s.trim();
//        return t.isEmpty() ? null : Normalizer.normalize(t, Normalizer.Form.NFC);
//    }
//
//    private String blankToNull(String s) {
//        if (s == null) return null;
//        String t = s.trim();
//        return t.isEmpty() ? null : t;
//    }
//}
