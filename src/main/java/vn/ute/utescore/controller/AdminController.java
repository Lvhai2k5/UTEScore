package vn.ute.utescore.controller;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import vn.ute.utescore.model.BaoTri;
import vn.ute.utescore.model.ConfigurationHistory;
import vn.ute.utescore.model.GiaThue;
import vn.ute.utescore.model.HoanTien;
import vn.ute.utescore.model.KhachHang;
import vn.ute.utescore.model.NhanVien;
import vn.ute.utescore.model.Roles;
import vn.ute.utescore.model.SanBong;
import vn.ute.utescore.model.ThanhToan;
import vn.ute.utescore.model.TinhNangSan;
import vn.ute.utescore.repository.ConfigurationHistoryRepository;
import vn.ute.utescore.repository.HoanTienRepository;
import vn.ute.utescore.repository.NhanVienRepository;
import vn.ute.utescore.repository.ThanhToanRepository;
import vn.ute.utescore.service.AdminService;
import vn.ute.utescore.service.SystemConfigService;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;
    
    @Autowired
    private NhanVienRepository nhanVienRepository;
    
    @Autowired
    private ThanhToanRepository thanhToanRepository;
    
    @Autowired
    private HoanTienRepository hoanTienRepository;
    
    @Autowired
    private SystemConfigService systemConfigService;
    
    @Autowired
    private ConfigurationHistoryRepository configurationHistoryRepository;

    // Dashboard
    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        // Thống kê tổng quan
        long totalFields = adminService.getTotalFields();
        long activeFields = adminService.getActiveFields();
        long maintenanceFields = adminService.getMaintenanceFields();
        long totalBookings = adminService.getTotalBookings();
        long todayBookings = adminService.getTodayBookings();
        
        // Tổng đơn đặt sân
        long totalBookingsCount = adminService.getTotalBookings();
        double bookingsChange = adminService.getBookingChangePercentage();
        
        // Doanh thu hôm nay
        java.math.BigDecimal todayRevenue = adminService.getTodayRevenue();
        java.math.BigDecimal yesterdayRevenue = adminService.getYesterdayRevenue();
        java.math.BigDecimal revenueChangeBD = adminService.calculatePercentageChange(todayRevenue, yesterdayRevenue);
        double revenueChange = revenueChangeBD != null ? revenueChangeBD.doubleValue() : 0.0;
        
        // Sân đang hoạt động
        long activeFieldsCount = adminService.getActiveFields();
        long totalFieldsCount = adminService.getTotalFields();
        double fieldUtilization = adminService.getFieldUtilizationRate();
        
        // Khách hàng mới
        long newCustomersThisWeek = adminService.getNewCustomersThisWeek();
        double customerChange = adminService.getCustomerChangePercentage();
        
        // Add to model
        model.addAttribute("totalBookingsCount", totalBookingsCount);
        model.addAttribute("bookingsChange", bookingsChange);
        model.addAttribute("todayRevenue", todayRevenue);
        model.addAttribute("revenueChange", revenueChange);
        model.addAttribute("activeFieldsCount", activeFieldsCount);
        model.addAttribute("totalFieldsCount", totalFieldsCount);
        model.addAttribute("fieldUtilization", fieldUtilization);
        model.addAttribute("newCustomersThisWeek", newCustomersThisWeek);
        model.addAttribute("customerChange", customerChange);
        
        // Keep old attributes for compatibility
        model.addAttribute("totalFields", totalFields);
        model.addAttribute("activeFields", activeFields);
        model.addAttribute("maintenanceFields", maintenanceFields);
        model.addAttribute("totalBookings", totalBookings);
        model.addAttribute("todayBookings", todayBookings);
        
        return "admin/dashboard";
    }

    // Quản lý sân bóng
    @GetMapping("/fields")
    public String fieldManagement(Model model) {
        List<SanBong> fields = adminService.getAllFields();
        List<NhanVien> employees = adminService.getAllEmployees();
        
        model.addAttribute("fields", fields);
        model.addAttribute("employees", employees);
        
        return "admin/field-management";
    }

    // Thêm sân mới
    @PostMapping("/fields/add")
    @ResponseBody
    public String addField(@RequestBody java.util.Map<String, Object> request) {
        try {
            System.out.println("Received request: " + request);
            
            SanBong field = new SanBong();
            field.setTenSan((String) request.get("tenSan"));
            field.setLoaiSan((String) request.get("loaiSan"));
            field.setKhuVuc((String) request.get("khuVuc"));
            field.setDuongDanGGMap((String) request.get("duongDanGGMap"));
            field.setMoTa((String) request.get("moTa"));
            field.setTrangThai((String) request.get("trangThai"));
            
            // Parse time fields - handle both formats
            if (request.get("gioMoCua") != null && !((String) request.get("gioMoCua")).isEmpty()) {
                try {
                    String gioMoCua = (String) request.get("gioMoCua");
                    System.out.println("Parsing gioMoCua: " + gioMoCua);
                    
                    // Try AM/PM format first
                    if (gioMoCua.contains("AM") || gioMoCua.contains("PM")) {
                        java.time.format.DateTimeFormatter formatter = java.time.format.DateTimeFormatter.ofPattern("hh:mm a", java.util.Locale.ENGLISH);
                        field.setGioMoCua(java.time.LocalTime.parse(gioMoCua, formatter));
                    } else {
                        // Try HH:mm format
                        field.setGioMoCua(java.time.LocalTime.parse(gioMoCua));
                    }
                } catch (Exception e1) {
                    System.out.println("Error parsing gioMoCua: " + e1.getMessage());
                    field.setGioMoCua(java.time.LocalTime.of(6, 0));
                }
            }
            
            if (request.get("gioDongCua") != null && !((String) request.get("gioDongCua")).isEmpty()) {
                try {
                    String gioDongCua = (String) request.get("gioDongCua");
                    System.out.println("Parsing gioDongCua: " + gioDongCua);
                    
                    // Try AM/PM format first
                    if (gioDongCua.contains("AM") || gioDongCua.contains("PM")) {
                        java.time.format.DateTimeFormatter formatter = java.time.format.DateTimeFormatter.ofPattern("hh:mm a", java.util.Locale.ENGLISH);
                        field.setGioDongCua(java.time.LocalTime.parse(gioDongCua, formatter));
                    } else {
                        field.setGioDongCua(java.time.LocalTime.parse(gioDongCua));
                    }
                } catch (Exception e1) {
                    System.out.println("Error parsing gioDongCua: " + e1.getMessage());
                    field.setGioDongCua(java.time.LocalTime.of(22, 0));
                }
            }
            
            // Get featureIds
            @SuppressWarnings("unchecked")
            List<Integer> featureIds = (List<Integer>) request.get("featureIds");
            System.out.println("FeatureIds: " + featureIds);
            
            adminService.addField(field, featureIds);
            return "success";
        } catch (Exception e) {
            System.err.println("Error adding field: " + e.getMessage());
            e.printStackTrace();
            return "error: " + e.getMessage();
        }
    }

    // Cập nhật sân
    @PostMapping("/fields/{id}/update")
    @ResponseBody
    public String updateField(@PathVariable Integer id, @RequestBody java.util.Map<String, Object> request) {
        try {
            System.out.println("Updating field ID: " + id);
            System.out.println("Received data: " + request);
            
            // Load existing field
            SanBong field = adminService.getFieldById(id);
            if (field == null) {
                return "error: Sân không tồn tại";
            }
            
            // Update fields
            if (request.containsKey("tenSan")) {
                field.setTenSan((String) request.get("tenSan"));
            }
            if (request.containsKey("loaiSan")) {
                field.setLoaiSan((String) request.get("loaiSan"));
            }
            if (request.containsKey("khuVuc")) {
                field.setKhuVuc((String) request.get("khuVuc"));
            }
            if (request.containsKey("moTa")) {
                field.setMoTa((String) request.get("moTa"));
            }
            if (request.containsKey("trangThai")) {
                field.setTrangThai((String) request.get("trangThai"));
            }
            
            // Parse time fields
            if (request.containsKey("gioMoCua")) {
                String gioMoCua = (String) request.get("gioMoCua");
                if (gioMoCua != null && !gioMoCua.isEmpty()) {
                    try {
                        field.setGioMoCua(java.time.LocalTime.parse(gioMoCua));
                    } catch (Exception e) {
                        System.out.println("Error parsing gioMoCua: " + e.getMessage());
                    }
                }
            }
            
            if (request.containsKey("gioDongCua")) {
                String gioDongCua = (String) request.get("gioDongCua");
                if (gioDongCua != null && !gioDongCua.isEmpty()) {
                    try {
                        field.setGioDongCua(java.time.LocalTime.parse(gioDongCua));
                    } catch (Exception e) {
                        System.out.println("Error parsing gioDongCua: " + e.getMessage());
                    }
                }
            }
            
            field.setNgayCapNhat(LocalDateTime.now());
            adminService.saveField(field);
            return "success";
        } catch (Exception e) {
            System.err.println("Error updating field: " + e.getMessage());
            e.printStackTrace();
            return "error: " + e.getMessage();
        }
    }

    // Xóa sân
    @DeleteMapping("/fields/{id}")
    @ResponseBody
    public String deleteField(@PathVariable Integer id) {
        try {
            adminService.deleteField(id);
            return "success";
        } catch (Exception e) {
            return "error: " + e.getMessage();
        }
    }

    // Thay đổi trạng thái sân
    @PostMapping("/fields/{id}/status")
    @ResponseBody
    public String changeFieldStatus(@PathVariable Integer id, @RequestParam String status, @RequestParam String reason) {
        try {
            adminService.changeFieldStatus(id, status, reason);
            return "success";
        } catch (Exception e) {
            return "error: " + e.getMessage();
        }
    }

    // Đặt lịch bảo trì
    @PostMapping("/maintenance/schedule")
    @ResponseBody
    public String scheduleMaintenance(@RequestBody java.util.Map<String, Object> request) {
        try {
            System.out.println("Received maintenance request: " + request);
            
            BaoTri maintenance = new BaoTri();
            
            // Parse maSan - load actual SanBong object from repository
            if (request.get("maSan") != null) {
                Integer maSan = ((Number) request.get("maSan")).intValue();
                SanBong sanBong = adminService.getFieldById(maSan);
                if (sanBong == null) {
                    return "error: Không tìm thấy sân với ID: " + maSan;
                }
                maintenance.setSanBong(sanBong);
            }
            
            // Parse nhanVienId - load actual NhanVien object from repository
            if (request.get("nhanVienId") != null) {
                Integer nhanVienId = ((Number) request.get("nhanVienId")).intValue();
                // Load NhanVien from database
                NhanVien nhanVien = nhanVienRepository.findById(nhanVienId).orElse(null);
                if (nhanVien == null) {
                    return "error: Không tìm thấy nhân viên với ID: " + nhanVienId;
                }
                maintenance.setNhanVien(nhanVien);
            }
            
            // Parse times
            if (request.get("thoiGianBatDau") != null) {
                String timeStr = (String) request.get("thoiGianBatDau");
                maintenance.setThoiGianBatDau(java.time.LocalTime.parse(timeStr));
            }
            if (request.get("thoiGianKetThuc") != null) {
                String timeStr = (String) request.get("thoiGianKetThuc");
                maintenance.setThoiGianKetThuc(java.time.LocalTime.parse(timeStr));
            }
            
            // Parse date
            if (request.get("ngayBaoTri") != null) {
                String dateStr = (String) request.get("ngayBaoTri");
                maintenance.setNgayBaoTri(java.time.LocalDate.parse(dateStr).atStartOfDay());
            }
            
            maintenance.setLyDo((String) request.get("lyDo"));
            maintenance.setTrangThai("Đã lên lịch");
            
            // Kiểm tra xung đột với lịch thuê hiện có
            boolean hasConflict = adminService.checkMaintenanceConflict(maintenance);
            if (hasConflict) {
                return "conflict";
            }
            
            adminService.scheduleMaintenance(maintenance);
            return "success";
        } catch (Exception e) {
            System.err.println("Error scheduling maintenance: " + e.getMessage());
            e.printStackTrace();
            return "error: " + e.getMessage();
        }
    }
    
    // Kiểm tra xung đột chi tiết trước khi đặt lịch bảo trì
    @PostMapping("/maintenance/check-conflict")
    @ResponseBody
    public java.util.Map<String, Object> checkMaintenanceConflict(@RequestBody BaoTri maintenance) {
        return adminService.checkMaintenanceConflictDetails(maintenance);
    }

    // Lấy danh sách lịch bảo trì
    @GetMapping("/maintenance/list")
    @ResponseBody
    public List<vn.ute.utescore.dto.MaintenanceDTO> getMaintenanceList(@RequestParam(required = false) String fieldId,
                                         @RequestParam(required = false) String status) {
        try {
            System.out.println("Getting maintenance list - fieldId: " + fieldId + ", status: " + status);
            List<vn.ute.utescore.dto.MaintenanceDTO> maintenanceList = adminService.getMaintenanceListDTOWithDynamicStatus(fieldId, status);
            System.out.println("Found " + maintenanceList.size() + " maintenance schedules");
            return maintenanceList;
        } catch (Exception e) {
            System.err.println("Error getting maintenance list: " + e.getMessage());
            e.printStackTrace();
            return new java.util.ArrayList<>();
        }
    }

    // Cập nhật trạng thái bảo trì
    @PostMapping("/maintenance/{id}/status")
    @ResponseBody
    public String updateMaintenanceStatus(@PathVariable Integer id, @RequestParam String status) {
        try {
            adminService.updateMaintenanceStatus(id, status);
            return "success";
        } catch (Exception e) {
            return "error: " + e.getMessage();
        }
    }
    
    // Xóa lịch bảo trì
    @DeleteMapping("/maintenance/{id}")
    @ResponseBody
    public String deleteMaintenance(@PathVariable Integer id) {
        try {
            adminService.deleteMaintenance(id);
            return "success";
        } catch (Exception e) {
            return "error: " + e.getMessage();
        }
    }
    
    // Hủy lịch bảo trì (không xóa, chỉ đổi trạng thái)
    @PostMapping("/maintenance/{id}/cancel")
    @ResponseBody
    public String cancelMaintenance(@PathVariable Integer id) {
        try {
            adminService.cancelMaintenance(id);
            return "success";
        } catch (Exception e) {
            return "error: " + e.getMessage();
        }
    }
    
    // Xuất báo cáo bảo trì theo tháng
    @GetMapping("/maintenance/report/month")
    @ResponseBody
    public List<vn.ute.utescore.dto.MaintenanceDTO> getMaintenanceReportByMonth(
            @RequestParam int year, 
            @RequestParam int month) {
        try {
            System.out.println("=== Requesting monthly report for year: " + year + ", month: " + month);
            List<vn.ute.utescore.dto.MaintenanceDTO> result = adminService.getMaintenanceReportByMonth(year, month);
            System.out.println("=== Found " + result.size() + " maintenance records");
            result.forEach(m -> System.out.println("  - " + m.getTenSan() + " - " + m.getTrangThai() + " - " + m.getNgayBaoTri()));
            return result;
        } catch (Exception e) {
            System.err.println("Error getting monthly report: " + e.getMessage());
            e.printStackTrace();
            return new java.util.ArrayList<>();
        }
    }
    
    // Xuất báo cáo bảo trì theo quý
    @GetMapping("/maintenance/report/quarter")
    @ResponseBody
    public List<vn.ute.utescore.dto.MaintenanceDTO> getMaintenanceReportByQuarter(
            @RequestParam int year, 
            @RequestParam int quarter) {
        try {
            return adminService.getMaintenanceReportByQuarter(year, quarter);
        } catch (Exception e) {
            System.err.println("Error getting quarterly report: " + e.getMessage());
            return new java.util.ArrayList<>();
        }
    }
    
    // Xuất báo cáo bảo trì theo năm
    @GetMapping("/maintenance/report/year")
    @ResponseBody
    public List<vn.ute.utescore.dto.MaintenanceDTO> getMaintenanceReportByYear(
            @RequestParam int year) {
        try {
            return adminService.getMaintenanceReportByYear(year);
        } catch (Exception e) {
            System.err.println("Error getting yearly report: " + e.getMessage());
            return new java.util.ArrayList<>();
        }
    }

    // Tìm kiếm và lọc sân
    @GetMapping("/fields/search")
    @ResponseBody
    public List<SanBong> searchFields(@RequestParam(required = false) String keyword,
                                    @RequestParam(required = false) String status,
                                    @RequestParam(required = false) String type,
                                    @RequestParam(required = false) String area) {
        return adminService.searchFields(keyword, status, type, area);
    }
    
    // Tìm kiếm sân nâng cao
    @GetMapping("/fields/search-advanced")
    @ResponseBody
    public List<SanBong> searchFieldsAdvanced(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String area,
            @RequestParam(required = false) List<Integer> featureIds,
            @RequestParam(required = false) Integer nhanVienId,
            @RequestParam(required = false) String sortBy) {
        return adminService.searchFieldsAdvanced(keyword, status, type, area, featureIds, nhanVienId, sortBy);
    }
    
    // Lấy tất cả tính năng
    @GetMapping("/features")
    @ResponseBody
    public List<vn.ute.utescore.model.TinhNang> getAllFeatures() {
        return adminService.getAllFeatures();
    }
    
    // Lấy danh sách loại sân
    @GetMapping("/field-types")
    @ResponseBody
    public List<String> getFieldTypes() {
        return adminService.getAllFieldTypes();
    }
    
    // Lấy danh sách khu vực
    @GetMapping("/areas")
    @ResponseBody
    public List<String> getAreas() {
        return adminService.getAllAreas();
    }
    
    // Mở sân
    @PostMapping("/fields/{id}/open")
    @ResponseBody
    public String openField(@PathVariable Integer id, @RequestParam(required = false) String reason) {
        try {
            adminService.openField(id, reason != null ? reason : "Sân được mở lại");
            return "success";
        } catch (Exception e) {
            return "error: " + e.getMessage();
        }
    }
    
    // Tạm đóng sân
    @PostMapping("/fields/{id}/close")
    @ResponseBody
    public String closeField(@PathVariable Integer id, @RequestParam String reason) {
        try {
            adminService.closeField(id, reason);
            return "success";
        } catch (Exception e) {
            return "error: " + e.getMessage();
        }
    }
    
    // Đóng sân vĩnh viễn
    @PostMapping("/fields/{id}/permanently-close")
    @ResponseBody
    public String permanentlyCloseField(@PathVariable Integer id, @RequestParam String reason) {
        try {
            adminService.permanentlyCloseField(id, reason);
            return "success";
        } catch (Exception e) {
            return "error: " + e.getMessage();
        }
    }
    
    // Lấy chi tiết sân (trả về JSON an toàn, tránh vòng lặp quan hệ)
    @GetMapping("/fields/{id}")
    @ResponseBody
    public java.util.Map<String, Object> getFieldDetails(@PathVariable Integer id) {
        SanBong f = adminService.getFieldById(id);
        if (f == null) return new java.util.HashMap<>();
        java.util.Map<String, Object> m = new java.util.HashMap<>();
        m.put("maSan", f.getMaSan());
        m.put("tenSan", f.getTenSan());
        m.put("loaiSan", f.getLoaiSan());
        m.put("khuVuc", f.getKhuVuc());
        m.put("duongDanGGMap", f.getDuongDanGGMap());
        m.put("gioMoCua", f.getGioMoCua() != null ? f.getGioMoCua().toString() : null);
        m.put("gioDongCua", f.getGioDongCua() != null ? f.getGioDongCua().toString() : null);
        m.put("moTa", f.getMoTa());
        m.put("hinhAnh", f.getHinhAnh());
        m.put("trangThai", f.getTrangThai());
        m.put("ngayTao", f.getNgayTao());
        m.put("ngayCapNhat", f.getNgayCapNhat());
        return m;
    }
    
    // API lấy danh sách sân (JSON an toàn)
    @GetMapping("/fields/list")
    @ResponseBody
    public java.util.List<java.util.Map<String, Object>> getAllFieldsJson() {
        System.out.println("Getting all fields...");
        List<SanBong> fields = adminService.getAllFields();
        System.out.println("Found " + fields.size() + " fields");
        java.util.List<java.util.Map<String, Object>> result = new java.util.ArrayList<>();
        for (SanBong f : fields) {
            java.util.Map<String, Object> m = new java.util.HashMap<>();
            m.put("maSan", f.getMaSan());
            m.put("tenSan", f.getTenSan());
            m.put("loaiSan", f.getLoaiSan());
            m.put("khuVuc", f.getKhuVuc());
            m.put("duongDanGGMap", f.getDuongDanGGMap());
            m.put("gioMoCua", f.getGioMoCua() != null ? f.getGioMoCua().toString() : null);
            m.put("gioDongCua", f.getGioDongCua() != null ? f.getGioDongCua().toString() : null);
            m.put("moTa", f.getMoTa());
            m.put("hinhAnh", f.getHinhAnh());
            m.put("trangThai", f.getTrangThai());
            m.put("ngayTao", f.getNgayTao());
            m.put("ngayCapNhat", f.getNgayCapNhat());
            result.add(m);
        }
        return result;
    }

    // Quản lý giá thuê
    @GetMapping("/pricing")
    public String pricingManagement(Model model) {
        List<GiaThue> pricingList = adminService.getAllPricing();
        model.addAttribute("pricingList", pricingList);
        return "admin/pricing-management";
    }

    // Thêm/cập nhật giá thuê
    @PostMapping("/pricing/save")
    @ResponseBody
    public String savePricing(@RequestBody GiaThue pricing) {
        try {
            pricing.setNgayApDung(LocalDateTime.now());
            adminService.savePricing(pricing);
            return "success";
        } catch (Exception e) {
            return "error: " + e.getMessage();
        }
    }

    // Quản lý tính năng sân
    @GetMapping("/field-features")
    @ResponseBody
    public List<TinhNangSan> getFieldFeatures(@RequestParam Integer fieldId) {
        return adminService.getFieldFeatures(fieldId);
    }

    // Cập nhật tính năng sân
    @PostMapping("/field-features/update")
    @ResponseBody
    public String updateFieldFeatures(@RequestParam Integer fieldId, @RequestBody List<TinhNangSan> features) {
        try {
            adminService.updateFieldFeatures(fieldId, features);
            return "success";
        } catch (Exception e) {
            return "error: " + e.getMessage();
        }
    }
    
    // ========== TRANG ADMIN KHÁC ==========
    
    // Trang login
    @GetMapping("/login")
    public String login() {
        return "admin/login";
    }
    
    // Trang index admin
    @GetMapping("/")
    public String index() {
        return "admin/index";
    }
    
    // Quản lý booking
    @GetMapping("/booking")
    public String booking() {
        return "admin/booking";
    }
    
    // Quản lý booking (chi tiết)
    @GetMapping("/booking-management")
    public String bookingManagement() {
        return "admin/booking-management";
    }
    
    // Quản lý thanh toán
    @GetMapping("/payment-management")
    public String paymentManagement(Model model) {
        // Load payment statistics using statistics API
        java.util.Map<String, Object> stats = getPaymentStatistics();
        
        model.addAttribute("totalPayments", stats.get("totalPayments"));
        model.addAttribute("successfulCount", stats.get("successfulCount"));
        model.addAttribute("pendingCount", stats.get("pendingCount"));
        model.addAttribute("failedCount", stats.get("failedCount"));
        model.addAttribute("totalRevenue", stats.get("totalRevenue"));
        model.addAttribute("revenueChangePercent", stats.get("revenueChangePercent"));
        model.addAttribute("successfulChangePercent", stats.get("successfulChangePercent"));
        
        // Load analytics data
        java.util.Map<String, Object> analytics = adminService.getPaymentAnalytics();
        model.addAttribute("analytics", analytics);
        
        // Load payment list directly from database using JPQL
        java.util.List<vn.ute.utescore.dto.PaymentDTO> payments = adminService.getAllPaymentsAsDTO();
        model.addAttribute("payments", payments);
        
        System.out.println("Loaded " + payments.size() + " payments for payment-management page");
        
        return "admin/payment-management";
    }
    
    // API to get all payments
    @GetMapping("/payments/list")
    @ResponseBody
    public java.util.List<java.util.Map<String, Object>> getAllPayments() {
        System.out.println("===== API call: /admin/payments/list =====");
        try {
            List<ThanhToan> payments = thanhToanRepository.findAll();
            System.out.println("===== Found " + payments.size() + " payments =====");
            
            // Convert to simple Map to avoid circular reference issues
            java.util.List<java.util.Map<String, Object>> result = new java.util.ArrayList<>();
            for (ThanhToan payment : payments) {
                try {
                    java.util.Map<String, Object> map = new java.util.HashMap<>();
                    map.put("maThanhToan", payment.getMaThanhToan());
                    // Get maDonDat from thueSan relationship
                    Integer maDonDat = (payment.getThueSan() != null) ? payment.getThueSan().getMaDonDat() : null;
                    map.put("maDonDat", maDonDat);
                    map.put("soTienNhan", payment.getSoTienNhan());
                    map.put("phuongThuc", payment.getPhuongThuc());
                    map.put("trangThaiThanhToan", payment.getTrangThaiThanhToan());
                    map.put("ngayThanhToan", payment.getNgayThanhToan());
                    map.put("maGiaoDich", payment.getMaGiaoDich());
                    map.put("ghiChu", payment.getGhiChu());
                    map.put("loaiThanhToan", payment.getLoaiThanhToan());
                    map.put("soDiemSuDung", payment.getSoDiemSuDung());
                    map.put("giaTriDiemGiam", payment.getGiaTriDiemGiam());
                    map.put("soDiemCongThem", payment.getSoDiemCongThem());
                    result.add(map);
                } catch (Exception e) {
                    System.err.println("Error processing payment: " + e.getMessage());
                }
            }
            
            System.out.println("===== Returning " + result.size() + " payment maps =====");
            return result;
        } catch (Exception e) {
            System.err.println("===== ERROR in getAllPayments: " + e.getMessage() + " =====");
            e.printStackTrace();
            return new java.util.ArrayList<>();
        }
    }
    
    // API to get payment statistics with comparison
    @GetMapping("/payments/statistics")
    @ResponseBody
    public java.util.Map<String, Object> getPaymentStatistics() {
        java.util.Map<String, Object> stats = new java.util.HashMap<>();
        
        // Get all payments
        List<ThanhToan> allPayments = thanhToanRepository.findAll();
        
        // 1. Tổng doanh thu: Doanh thu gộp (tổng SoTienNhan của giao dịch thành công) - Tổng tiền hoàn
        java.math.BigDecimal grossRevenue = allPayments.stream()
            .filter(p -> "Thành công".equals(p.getTrangThaiThanhToan()))
            .map(ThanhToan::getSoTienNhan)
            .filter(java.util.Objects::nonNull)
            .reduce(java.math.BigDecimal.ZERO, java.math.BigDecimal::add);
        
        // Get total refund amount
        java.math.BigDecimal totalRefund = adminService.getTotalRefundAmount();
        
        // Net revenue = Gross Revenue - Total Refunds
        java.math.BigDecimal totalRevenue = grossRevenue.subtract(totalRefund);
        
        // 2. Giao dịch thành công: Đếm số giao dịch có trạng thái "Thành công"
        long successfulCount = allPayments.stream()
            .filter(p -> "Thành công".equals(p.getTrangThaiThanhToan()))
            .count();
        
        // 3. Giao dịch đang chờ xử lý: Đếm số giao dịch có trạng thái "Đang xử lý"
        long pendingCount = allPayments.stream()
            .filter(p -> "Đang xử lý".equals(p.getTrangThaiThanhToan()))
            .count();
        
        // 4. Giao dịch lỗi: Đếm số giao dịch có trạng thái "Thất bại" hoặc "Hủy bỏ"
        long failedCount = allPayments.stream()
            .filter(p -> "Thất bại".equals(p.getTrangThaiThanhToan()) || "Hủy bỏ".equals(p.getTrangThaiThanhToan()))
            .count();
        
        // Calculate comparison with last month
        java.time.LocalDateTime now = java.time.LocalDateTime.now();
        java.time.LocalDateTime firstDayOfMonth = now.withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0);
        java.time.LocalDateTime firstDayOfLastMonth = firstDayOfMonth.minusMonths(1);
        
        // Last month successful payments
        java.math.BigDecimal lastMonthRevenue = allPayments.stream()
            .filter(p -> p.getNgayThanhToan() != null 
                && p.getNgayThanhToan().isAfter(firstDayOfLastMonth)
                && p.getNgayThanhToan().isBefore(firstDayOfMonth))
            .map(ThanhToan::getSoTienNhan)
            .filter(java.util.Objects::nonNull)
            .reduce(java.math.BigDecimal.ZERO, java.math.BigDecimal::add);
        
        long lastMonthSuccessfulCount = allPayments.stream()
            .filter(p -> "Thành công".equals(p.getTrangThaiThanhToan())
                && p.getNgayThanhToan() != null 
                && p.getNgayThanhToan().isAfter(firstDayOfLastMonth)
                && p.getNgayThanhToan().isBefore(firstDayOfMonth))
            .count();
        
        // Calculate percentage changes
        double revenueChangePercent = 0.0;
        if (lastMonthRevenue.compareTo(java.math.BigDecimal.ZERO) > 0) {
            revenueChangePercent = totalRevenue.subtract(lastMonthRevenue)
                .divide(lastMonthRevenue, 4, java.math.RoundingMode.HALF_UP)
                .multiply(new java.math.BigDecimal("100"))
                .doubleValue();
        } else if (totalRevenue.compareTo(java.math.BigDecimal.ZERO) > 0) {
            revenueChangePercent = 100.0;
        }
        
        double successfulChangePercent = 0.0;
        if (lastMonthSuccessfulCount > 0) {
            successfulChangePercent = (successfulCount - lastMonthSuccessfulCount) * 100.0 / lastMonthSuccessfulCount;
        } else if (successfulCount > 0) {
            successfulChangePercent = 100.0;
        }
        
        stats.put("totalPayments", allPayments.size());
        stats.put("successfulCount", successfulCount);
        stats.put("pendingCount", pendingCount);
        stats.put("failedCount", failedCount);
        stats.put("totalRevenue", totalRevenue);
        stats.put("revenueChangePercent", revenueChangePercent);
        stats.put("successfulChangePercent", successfulChangePercent);
        
        return stats;
    }
    
    // API to get analytics data (top field, staff, area, payment method)
    @GetMapping("/payments/analytics")
    @ResponseBody
    public java.util.Map<String, Object> getPaymentAnalytics() {
        return adminService.getPaymentAnalytics();
    }
    
    // API to get revenue trend data for chart
    @GetMapping("/payments/revenue-trend")
    @ResponseBody
    public java.util.Map<String, Object> getRevenueTrend(@RequestParam(defaultValue = "30days") String period) {
        System.out.println("===== API call: /admin/payments/revenue-trend, period: " + period + " =====");
        
        List<ThanhToan> allPayments = thanhToanRepository.findAll();
        java.time.LocalDateTime now = java.time.LocalDateTime.now();
        
        // Group by period and calculate revenue
        java.util.Map<String, Long> revenueByPeriod = new java.util.HashMap<>();
        java.util.List<String> labels = new java.util.ArrayList<>();
        java.util.List<Long> data = new java.util.ArrayList<>();
        
        if ("7days".equals(period)) {
            // Group by day
            for (int i = 6; i >= 0; i--) {
                java.time.LocalDateTime day = now.minusDays(i).withHour(0).withMinute(0).withSecond(0);
                String label = day.format(java.time.format.DateTimeFormatter.ofPattern("dd/MM"));
                labels.add(label);
                data.add(0L);
                revenueByPeriod.put(label, 0L);
            }
            
            for (ThanhToan payment : allPayments) {
                if ("Thành công".equals(payment.getTrangThaiThanhToan()) 
                    && payment.getNgayThanhToan() != null 
                    && payment.getNgayThanhToan().isAfter(now.minusDays(7))
                    && payment.getSoTienNhan() != null) {
                    String dayKey = payment.getNgayThanhToan().format(java.time.format.DateTimeFormatter.ofPattern("dd/MM"));
                    revenueByPeriod.put(dayKey, 
                        revenueByPeriod.getOrDefault(dayKey, 0L) + payment.getSoTienNhan().longValue());
                }
            }
            
            // Subtract refunds from each day
            List<HoanTien> refunds = hoanTienRepository.findAll();
            java.math.BigDecimal totalRefund = refunds.stream()
                .filter(r -> "Đã hoàn".equals(r.getTrangThaiHoan()))
                .map(HoanTien::getSoTienHoan)
                .filter(java.util.Objects::nonNull)
                .reduce(java.math.BigDecimal.ZERO, java.math.BigDecimal::add);
            
            // Calculate daily refund portion (simple average)
            long dailyRefund = totalRefund.divide(new java.math.BigDecimal("7"), 0, java.math.RoundingMode.HALF_UP).longValue();
            
            // Subtract refunds from revenue by period
            for (String label : labels) {
                if (revenueByPeriod.containsKey(label)) {
                    long revenue = revenueByPeriod.get(label);
                    revenueByPeriod.put(label, Math.max(0, revenue - dailyRefund));
                } else {
                    revenueByPeriod.put(label, 0L);
                }
            }
            
            data = new java.util.ArrayList<>();
            for (String label : labels) {
                data.add(revenueByPeriod.getOrDefault(label, 0L));
            }
        } else if ("30days".equals(period)) {
            // Group by week - show all payments
            for (int i = 3; i >= 0; i--) {
                String label = "Tuần " + (4 - i);
                labels.add(label);
                data.add(0L);
            }
            
            for (ThanhToan payment : allPayments) {
                if ("Thành công".equals(payment.getTrangThaiThanhToan())
                    && payment.getNgayThanhToan() != null && payment.getSoTienNhan() != null) {
                    long daysAgo = java.time.Duration.between(payment.getNgayThanhToan(), now).toDays();
                    // Put all payments into one of the 4 weeks, even if older than 30 days
                    int week = (int) (daysAgo / 7);
                    if (week < 0) week = 0;
                    if (week > 3) week = 3;
                    data.set(week, data.get(week) + payment.getSoTienNhan().longValue());
                }
            }
            
            // Subtract refunds from revenue
            List<HoanTien> refunds = hoanTienRepository.findAll();
            java.math.BigDecimal totalRefund = refunds.stream()
                .filter(r -> "Đã hoàn".equals(r.getTrangThaiHoan()))
                .map(HoanTien::getSoTienHoan)
                .filter(java.util.Objects::nonNull)
                .reduce(java.math.BigDecimal.ZERO, java.math.BigDecimal::add);
            
            // Calculate weekly refund portion
            long weeklyRefund = totalRefund.divide(new java.math.BigDecimal("4"), 0, java.math.RoundingMode.HALF_UP).longValue();
            
            // Subtract refunds from each week
            for (int i = 0; i < data.size(); i++) {
                data.set(i, Math.max(0, data.get(i) - weeklyRefund));
            }
        } else {
            // Group by month
            for (int i = 2; i >= 0; i--) {
                java.time.LocalDateTime month = now.minusMonths(i);
                String label = "Tháng " + month.getMonthValue();
                labels.add(label);
                data.add(0L);
            }
            
            for (ThanhToan payment : allPayments) {
                if ("Thành công".equals(payment.getTrangThaiThanhToan())
                    && payment.getNgayThanhToan() != null 
                    && payment.getNgayThanhToan().isAfter(now.minusDays(90))
                    && payment.getSoTienNhan() != null) {
                    int monthsAgo = (int) java.time.temporal.ChronoUnit.MONTHS.between(payment.getNgayThanhToan(), now);
                    if (monthsAgo >= 0 && monthsAgo < 3) {
                        data.set(2 - monthsAgo, data.get(2 - monthsAgo) + payment.getSoTienNhan().longValue());
                    }
                }
            }
            
            // Subtract refunds from revenue
            List<HoanTien> refunds = hoanTienRepository.findAll();
            java.math.BigDecimal totalRefund = refunds.stream()
                .filter(r -> "Đã hoàn".equals(r.getTrangThaiHoan()))
                .map(HoanTien::getSoTienHoan)
                .filter(java.util.Objects::nonNull)
                .reduce(java.math.BigDecimal.ZERO, java.math.BigDecimal::add);
            
            // Calculate monthly refund portion
            long monthlyRefund = totalRefund.divide(new java.math.BigDecimal("3"), 0, java.math.RoundingMode.HALF_UP).longValue();
            
            // Subtract refunds from each month
            for (int i = 0; i < data.size(); i++) {
                data.set(i, Math.max(0, data.get(i) - monthlyRefund));
            }
        }
        
        java.util.Map<String, Object> result = new java.util.HashMap<>();
        result.put("labels", labels);
        result.put("data", data);
        
        return result;
    }
    
    // API to get transaction status distribution for pie chart
    @GetMapping("/payments/status-distribution")
    @ResponseBody
    public java.util.Map<String, Object> getStatusDistribution() {
        System.out.println("===== API call: /admin/payments/status-distribution =====");
        
        List<ThanhToan> allPayments = thanhToanRepository.findAll();
        
        long successfulCount = allPayments.stream()
            .filter(p -> "Thành công".equals(p.getTrangThaiThanhToan()))
            .count();
        
        long pendingCount = allPayments.stream()
            .filter(p -> "Đang xử lý".equals(p.getTrangThaiThanhToan()))
            .count();
        
        long failedCount = allPayments.stream()
            .filter(p -> "Thất bại".equals(p.getTrangThaiThanhToan()) || "Hủy bỏ".equals(p.getTrangThaiThanhToan()))
            .count();
        
        java.util.List<String> labels = new java.util.ArrayList<>();
        java.util.List<Long> data = new java.util.ArrayList<>();
        java.util.List<String> colors = new java.util.ArrayList<>();
        
        if (successfulCount > 0) {
            labels.add("Thành công");
            data.add(successfulCount);
            colors.add("#22c55e");
        }
        
        if (pendingCount > 0) {
            labels.add("Đang xử lý");
            data.add(pendingCount);
            colors.add("#f59e0b");
        }
        
        if (failedCount > 0) {
            labels.add("Thất bại");
            data.add(failedCount);
            colors.add("#ef4444");
        }
        
        java.util.Map<String, Object> result = new java.util.HashMap<>();
        result.put("labels", labels);
        result.put("data", data);
        result.put("colors", colors);
        
        return result;
    }
    
    // Quản lý hoàn tiền
    @GetMapping("/refund-management")
    public String refundManagement(Model model) {
        // Get refund statistics
        java.util.Map<String, Object> stats = getRefundStatistics();
        model.addAttribute("pendingCount", stats.get("pendingCount"));
        model.addAttribute("approvedCount", stats.get("approvedCount"));
        model.addAttribute("rejectedCount", stats.get("rejectedCount"));
        model.addAttribute("totalRefund", stats.get("totalRefund"));
        model.addAttribute("revenueImpact", stats.get("revenueImpact"));
        
        return "admin/refund-management";
    }
    
    // API to get refund statistics
    @GetMapping("/refunds/statistics")
    @ResponseBody
    public java.util.Map<String, Object> getRefundStatistics() {
        java.util.Map<String, Object> stats = new java.util.HashMap<>();
        
        // Get refund counts by status
        long pendingCount = adminService.getPendingRefundCount();
        long completedCount = adminService.getCompletedRefundCount();
        long rejectedCount = adminService.getRejectedRefundCount();
        long completedTodayCount = adminService.getCompletedRefundCountToday();
        
        // Calculate total refund amount
        java.math.BigDecimal totalRefund = adminService.getTotalRefundAmount();
        
        // Calculate revenue impact (total gross revenue - total refunds)
        java.math.BigDecimal revenueImpact = adminService.calculateRevenueImpact();
        
        // Get total gross revenue
        java.math.BigDecimal grossRevenue = adminService.getTotalGrossRevenue();
        
        // Calculate refund rate (percentage)
        double refundRate = 0.0;
        if (grossRevenue.compareTo(java.math.BigDecimal.ZERO) > 0) {
            refundRate = totalRefund.divide(grossRevenue, 4, java.math.RoundingMode.HALF_UP)
                    .multiply(java.math.BigDecimal.valueOf(100))
                    .doubleValue();
        }
        
        stats.put("pendingCount", pendingCount);
        stats.put("completedCount", completedCount);
        stats.put("completedTodayCount", completedTodayCount);
        stats.put("rejectedCount", rejectedCount);
        stats.put("totalRefund", totalRefund);
        stats.put("revenueImpact", revenueImpact);
        stats.put("grossRevenue", grossRevenue);
        stats.put("refundRate", refundRate);
        
        return stats;
    }
    
    // API to get all refunds
    @GetMapping("/refunds/list")
    @ResponseBody
    public java.util.List<java.util.Map<String, Object>> getAllRefunds() {
        List<HoanTien> refunds = adminService.getAllRefunds();
        return convertRefundsToMap(refunds);
    }
    
    // API to get refund analysis by reason
    @GetMapping("/refunds/reasons")
    @ResponseBody
    public java.util.Map<String, Object> getRefundsByReason() {
        return adminService.getRefundsByReason();
    }
    
    // Helper method to convert refunds to Map
    private java.util.List<java.util.Map<String, Object>> convertRefundsToMap(List<HoanTien> refunds) {
        java.util.List<java.util.Map<String, Object>> result = new java.util.ArrayList<>();
        for (HoanTien refund : refunds) {
            java.util.Map<String, Object> map = new java.util.HashMap<>();
            map.put("maHoanTien", refund.getMaHoanTien());
            
            // Map MaThanhToan từ relationship
            Integer maThanhToan = null;
            if (refund.getThanhToan() != null) {
                maThanhToan = refund.getThanhToan().getMaThanhToan();
            }
            map.put("maThanhToan", maThanhToan);
            
            map.put("soTienHoan", refund.getSoTienHoan() != null ? refund.getSoTienHoan() : 0);
            map.put("lyDoHoan", refund.getLyDoHoan());
            map.put("ngayYeuCau", refund.getNgayYeuCau());
            map.put("ngayXuLy", refund.getNgayXuLy());
            map.put("trangThaiHoan", refund.getTrangThaiHoan());
            map.put("phuongThucHoan", refund.getPhuongThucHoan());
            map.put("nguoiXuLy", refund.getNguoiXuLy());
            result.add(map);
        }
        return result;
    }
    
    // API to approve refund
    @PostMapping("/refunds/{id}/approve")
    @ResponseBody
    public String approveRefund(@PathVariable Integer id) {
        try {
            adminService.approveRefund(id);
            return "success";
        } catch (Exception e) {
            return "error: " + e.getMessage();
        }
    }
    
    // API to reject refund
    @PostMapping("/refunds/{id}/reject")
    @ResponseBody
    public String rejectRefund(@PathVariable Integer id, @RequestParam String reason) {
        try {
            adminService.rejectRefund(id, reason);
            return "success";
        } catch (Exception e) {
            return "error: " + e.getMessage();
        }
    }
    
    // API to get refund trend data
    @GetMapping("/refunds/trend")
    @ResponseBody
    public java.util.Map<String, Object> getRefundTrend(@RequestParam(required = false, defaultValue = "30days") String period) {
        return adminService.getRefundTrendData(period);
    }
    
    // Quản lý người dùng
    @GetMapping("/user-management")
    public String userManagement() {
        return "admin/user-management";
    }
    
    // Quản lý hệ thống
    @GetMapping("/system-config")
    public String systemConfig(Model model) {
        // Load current configurations
        java.util.Map<String, String> configs = systemConfigService.getAllConfigurations();
        model.addAttribute("configs", configs);
        
        // Load configuration history
        java.util.List<ConfigurationHistory> histories = configurationHistoryRepository.findAllByOrderByCreatedAtDesc();
        // Limit to last 10 records for display
        model.addAttribute("histories", histories.size() > 10 ? histories.subList(0, 10) : histories);
        
        return "admin/system-config";
    }
    
    // API để lưu cấu hình
    @PostMapping("/system-config/save")
    @ResponseBody
    public java.util.Map<String, Object> saveSystemConfig(@RequestBody java.util.Map<String, Object> configData) {
        String changedBy = (String) configData.getOrDefault("changedBy", "Admin");
        
        System.out.println("===== Saving system configuration =====");
        System.out.println("Config data: " + configData);
        
        java.util.Map<String, Object> result = systemConfigService.saveConfiguration(configData, changedBy);
        
        return result;
    }
    
    // API để lấy config hiện tại
    @GetMapping("/system-config/get")
    @ResponseBody
    public java.util.Map<String, String> getCurrentConfig() {
        return systemConfigService.getAllConfigurations();
    }
    
    // Live cameras
    @GetMapping("/live-cameras")
    public String liveCameras() {
        return "admin/live-cameras";
    }
    
    // =============== USER MANAGEMENT APIs ===============
    
    // Lấy danh sách tất cả người dùng (khách hàng + nhân viên)
    @GetMapping("/users/list")
    @ResponseBody
    public List<vn.ute.utescore.dto.UserDTO> getAllUsers(@RequestParam(required = false) String type) {
        // Use DTO to avoid circular reference issues
        List<vn.ute.utescore.dto.UserDTO> allUsers = adminService.getAllUsersAsDTO();
        
        if ("customer".equals(type) || "customers".equals(type)) {
            return allUsers.stream().filter(u -> "customer".equals(u.getUserType())).collect(java.util.stream.Collectors.toList());
        } else if ("staff".equals(type) || "employees".equals(type)) {
            return allUsers.stream().filter(u -> "employee".equals(u.getUserType())).collect(java.util.stream.Collectors.toList());
        }
        
        return allUsers;
    }
    
    // Lấy danh sách khách hàng
    @GetMapping("/customers/list")
    @ResponseBody
    public List<KhachHang> getCustomersList() {
        return adminService.getAllCustomers();
    }
    
    // Lấy danh sách nhân viên
    @GetMapping("/employees/list")
    @ResponseBody
    public List<vn.ute.utescore.dto.EmployeeDTO> getEmployeesList() {
        return adminService.getAllStaffAsDTO();
    }
    
    // Tìm kiếm khách hàng
    @GetMapping("/customers/search")
    @ResponseBody
    public List<KhachHang> searchCustomers(@RequestParam(required = false) String keyword) {
        return adminService.searchCustomers(keyword);
    }
    
    // Tìm kiếm nhân viên
    @GetMapping("/employees/search")
    @ResponseBody
    public List<NhanVien> searchEmployees(@RequestParam(required = false) String keyword) {
        return adminService.searchEmployees(keyword);
    }
    
    // Thêm nhân viên mới
    @PostMapping("/employees/add")
    @ResponseBody
    public String addEmployee(@RequestBody java.util.Map<String, Object> request) {
        try {
            System.out.println("Received employee data: " + request);
            
            NhanVien employee = new NhanVien();
            employee.setFullName((String) request.get("fullName"));
            employee.setEmail((String) request.get("email"));
            employee.setPhone((String) request.get("phone"));
            
            // Set role
            if (request.get("roleId") != null) {
                Integer roleId = ((Number) request.get("roleId")).intValue();
                Roles role = adminService.getAllRoles().stream()
                    .filter(r -> r.getRoleID().equals(roleId))
                    .findFirst()
                    .orElse(null);
                if (role != null) {
                    employee.setRole(role);
                }
            }
            
            String status = (String) request.getOrDefault("status", "Active");
            employee.setStatus(status);
            
            // Get password from request
            String password = (String) request.get("password");
            
            // Get sendEmail flag from request (default: false if not provided)
            boolean sendEmail = request.containsKey("sendEmail") ? (Boolean) request.get("sendEmail") : false;
            
            adminService.addEmployee(employee, password, sendEmail);
            return "success";
        } catch (Exception e) {
            System.err.println("Error adding employee: " + e.getMessage());
            e.printStackTrace();
            return "error: " + e.getMessage();
        }
    }
    
    // Cập nhật trạng thái nhân viên
    @PostMapping("/employees/{id}/status")
    @ResponseBody
    public String updateEmployeeStatus(@PathVariable Integer id, @RequestParam String status) {
        try {
            adminService.updateEmployeeStatus(id, status);
            return "success";
        } catch (Exception e) {
            return "error: " + e.getMessage();
        }
    }
    
    // Reset mật khẩu
    @PostMapping("/users/{email}/reset-password")
    @ResponseBody
    public java.util.Map<String, Object> resetPassword(@PathVariable String email) {
        java.util.Map<String, Object> response = new java.util.HashMap<>();
        try {
            System.out.println("=== RESET PASSWORD REQUEST ===");
            System.out.println("Received email: " + email);
            
            String newPassword = adminService.resetPassword(email);
            response.put("success", true);
            response.put("message", "Mật khẩu đã được đặt lại thành công");
            response.put("newPassword", newPassword);
            return response;
        } catch (Exception e) {
            System.err.println("Error in resetPassword: " + e.getMessage());
            e.printStackTrace();
            response.put("success", false);
            response.put("message", "Lỗi: " + e.getMessage());
            return response;
        }
    }
    
    // Khôi phục tài khoản khách hàng
    @PostMapping("/customers/{id}/recover")
    @ResponseBody
    public String recoverCustomer(@PathVariable Integer id) {
        try {
            adminService.recoverCustomer(id);
            return "success";
        } catch (Exception e) {
            return "error: " + e.getMessage();
        }
    }
    
    // Lấy danh sách tài khoản đã xóa
    @GetMapping("/customers/deleted")
    @ResponseBody
    public List<KhachHang> getDeletedCustomers() {
        return adminService.getDeletedCustomers();
    }
    
    // Lấy danh sách tài khoản bị khóa (khách hàng đã xóa + nhân viên bị khóa)
    @GetMapping("/users/locked")
    @ResponseBody
    public List<vn.ute.utescore.dto.UserDTO> getLockedUsers() {
        return adminService.getAllLockedUsersAsDTO();
    }
    
    // Khôi phục tài khoản nhân viên
    @PostMapping("/employees/{id}/recover")
    @ResponseBody
    public String recoverEmployee(@PathVariable Integer id) {
        try {
            adminService.recoverEmployee(id);
            return "success";
        } catch (Exception e) {
            return "error: " + e.getMessage();
        }
    }
    
    // Lấy danh sách vai trò
    @GetMapping("/roles")
    @ResponseBody
    public List<vn.ute.utescore.dto.RoleDTO> getAllRoles() {
        return adminService.getAllRolesAsDTO();
    }
    
    // Cập nhật thông tin nhân viên
    @PostMapping("/employees/{id}/update")
    @ResponseBody
    public String updateEmployee(@PathVariable Integer id, @RequestBody java.util.Map<String, Object> request) {
        try {
            System.out.println("Received update request for employee ID: " + id);
            System.out.println("Data: " + request);
            
            NhanVien employee = nhanVienRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy nhân viên"));
            
            // Update fields if provided
            if (request.containsKey("fullName")) {
                employee.setFullName((String) request.get("fullName"));
            }
            if (request.containsKey("email")) {
                String newEmail = (String) request.get("email");
                // Check if email is already used by another employee
                nhanVienRepository.findByEmail(newEmail).ifPresent(existing -> {
                    if (!existing.getUserID().equals(id)) {
                        throw new RuntimeException("Email đã được sử dụng");
                    }
                });
                employee.setEmail(newEmail);
            }
            if (request.containsKey("phone")) {
                String newPhone = (String) request.get("phone");
                // Check if phone is already used by another employee
                nhanVienRepository.findByPhone(newPhone).ifPresent(existing -> {
                    if (!existing.getUserID().equals(id)) {
                        throw new RuntimeException("Số điện thoại đã được sử dụng");
                    }
                });
                employee.setPhone(newPhone);
            }
            
            // Update role if provided
            if (request.containsKey("roleId")) {
                Integer roleId = ((Number) request.get("roleId")).intValue();
                Roles role = adminService.getAllRoles().stream()
                    .filter(r -> r.getRoleID().equals(roleId))
                    .findFirst()
                    .orElse(null);
                if (role != null) {
                    employee.setRole(role);
                }
            }
            
            // Update status if provided
            if (request.containsKey("status")) {
                String status = (String) request.get("status");
                adminService.updateEmployeeStatus(id, status);
            }
            
            employee.setUpdateAt(LocalDateTime.now());
            nhanVienRepository.save(employee);
            
            return "success";
        } catch (Exception e) {
            System.err.println("Error updating employee: " + e.getMessage());
            e.printStackTrace();
            return "error: " + e.getMessage();
        }
    }
    
    // =============== REVENUE STATISTICS APIs ===============
    
    // Get revenue statistics for different periods
    @GetMapping("/revenue/statistics")
    @ResponseBody
    public java.util.Map<String, Object> getRevenueStatistics(@RequestParam(defaultValue = "day") String period) {
        java.util.Map<String, Object> result = new java.util.HashMap<>();
        try {
            vn.ute.utescore.dto.RevenueDTO revenue = adminService.getRevenueStatistics(period);
            result.put("success", true);
            result.put("data", revenue);
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", e.getMessage());
        }
        return result;
    }
    
    // Get today's revenue
    @GetMapping("/revenue/today")
    @ResponseBody
    public java.util.Map<String, Object> getTodayRevenue() {
        java.util.Map<String, Object> result = new java.util.HashMap<>();
        try {
            java.math.BigDecimal revenue = adminService.getTodayRevenue();
            result.put("success", true);
            result.put("revenue", revenue);
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", e.getMessage());
        }
        return result;
    }
    
    // Get this week's revenue
    @GetMapping("/revenue/week")
    @ResponseBody
    public java.util.Map<String, Object> getWeekRevenue() {
        java.util.Map<String, Object> result = new java.util.HashMap<>();
        try {
            java.math.BigDecimal revenue = adminService.getThisWeekRevenue();
            result.put("success", true);
            result.put("revenue", revenue);
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", e.getMessage());
        }
        return result;
    }
    
    // Get this month's revenue
    @GetMapping("/revenue/month")
    @ResponseBody
    public java.util.Map<String, Object> getMonthRevenue() {
        java.util.Map<String, Object> result = new java.util.HashMap<>();
        try {
            java.math.BigDecimal revenue = adminService.getThisMonthRevenue();
            result.put("success", true);
            result.put("revenue", revenue);
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", e.getMessage());
        }
        return result;
    }
    
    // Get this year's revenue
    @GetMapping("/revenue/year")
    @ResponseBody
    public java.util.Map<String, Object> getYearRevenue() {
        java.util.Map<String, Object> result = new java.util.HashMap<>();
        try {
            java.math.BigDecimal revenue = adminService.getThisYearRevenue();
            result.put("success", true);
            result.put("revenue", revenue);
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", e.getMessage());
        }
        return result;
    }
    
    // Get revenue comparison between periods
    @GetMapping("/revenue/comparison")
    @ResponseBody
    public java.util.Map<String, Object> getRevenueComparison(@RequestParam String period) {
        java.util.Map<String, Object> result = new java.util.HashMap<>();
        try {
            vn.ute.utescore.dto.RevenueDTO revenueDTO = adminService.getRevenueStatistics(period);
            result.put("success", true);
            result.put("currentRevenue", revenueDTO.getTotalRevenue());
            result.put("previousRevenue", revenueDTO.getPreviousPeriodRevenue());
            result.put("changePercentage", revenueDTO.getChangePercentage());
            result.put("isPositiveChange", revenueDTO.isPositiveChange());
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", e.getMessage());
        }
        return result;
    }
}

