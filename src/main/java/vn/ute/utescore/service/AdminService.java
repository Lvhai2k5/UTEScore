package vn.ute.utescore.service;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import vn.ute.utescore.dto.MaintenanceDTO;
import vn.ute.utescore.dto.PaymentDTO;
import vn.ute.utescore.model.BaoTri;
import vn.ute.utescore.model.Camera;
import vn.ute.utescore.model.GiaThue;
import vn.ute.utescore.model.HoanTien;
import vn.ute.utescore.model.KhachHang;
import vn.ute.utescore.model.LichSuTrangThaiSan;
import vn.ute.utescore.model.NhanVien;
import vn.ute.utescore.model.Roles;
import vn.ute.utescore.model.SanBong;
import vn.ute.utescore.model.TaiKhoan;
import vn.ute.utescore.model.ThanhToan;
import vn.ute.utescore.model.ThueSan;
import vn.ute.utescore.model.TinhNang;
import vn.ute.utescore.model.TinhNangSan;
import vn.ute.utescore.repository.BaoTriRepository;
import vn.ute.utescore.repository.GiaThueRepository;
import vn.ute.utescore.repository.HoanTienRepository;
import vn.ute.utescore.repository.KhachHangRepository;
import vn.ute.utescore.repository.LichSuTrangThaiSanRepository;
import vn.ute.utescore.repository.NhanVienRepository;
import vn.ute.utescore.repository.RolesRepository;
import vn.ute.utescore.repository.SanBongRepository;
import vn.ute.utescore.repository.TaiKhoanRepository;
import vn.ute.utescore.repository.ThanhToanRepository;
import vn.ute.utescore.repository.ThueSanRepository;
import vn.ute.utescore.repository.TinhNangRepository;
import vn.ute.utescore.repository.TinhNangSanRepository;

@Service
public class AdminService {

    @Autowired
    private SanBongRepository sanBongRepository;
    
    @Autowired
    private BaoTriRepository baoTriRepository;
    
    @Autowired
    private NhanVienRepository nhanVienRepository;
    
    @Autowired
    private ThueSanRepository thueSanRepository;
    
    @Autowired
    private GiaThueRepository giaThueRepository;
    
    @Autowired
    private TinhNangSanRepository tinhNangSanRepository;
    
    @Autowired
    private TinhNangRepository tinhNangRepository;
    
    @Autowired
    private LichSuTrangThaiSanRepository lichSuTrangThaiSanRepository;
    
    @Autowired
    private KhachHangRepository khachHangRepository;
    
    @Autowired
    private TaiKhoanRepository taiKhoanRepository;
    
    @Autowired
    private RolesRepository rolesRepository;
    
    @Autowired
    private ThanhToanRepository thanhToanRepository;
    
    @Autowired
    private HoanTienRepository hoanTienRepository;
    
    @Autowired
    private EmailService emailService;

    // Dùng chung PasswordEncoder để mã hoá/kiểm tra mật khẩu theo cơ chế của hệ thống (BCrypt)
    @Autowired
    private org.springframework.security.crypto.password.PasswordEncoder passwordEncoder;

    // =============== DASHBOARD STATISTICS ===============
    
    public long getTotalFields() {
        return sanBongRepository.count();
    }
    
    public long getActiveFields() {
        return sanBongRepository.countByTrangThai("Hoạt động");
    }
    
    public long getMaintenanceFields() {
        return sanBongRepository.countByTrangThai("Bảo trì");
    }
    
    public long getTotalBookings() {
        return thueSanRepository.count();
    }
    
    public long getTodayBookings() {
        LocalDateTime startOfDay = LocalDateTime.now().with(LocalTime.MIN);
        LocalDateTime endOfDay = LocalDateTime.now().with(LocalTime.MAX);
        return thueSanRepository.countByNgayTaoBetween(startOfDay, endOfDay);
    }
    
    public long getLastMonthBookings() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime firstDayOfMonth = now.withDayOfMonth(1).with(LocalTime.MIN);
        LocalDateTime firstDayOfLastMonth = firstDayOfMonth.minusMonths(1);
        return thueSanRepository.countByNgayTaoBetween(firstDayOfLastMonth, firstDayOfMonth);
    }
    
    public double getBookingChangePercentage() {
        long currentBookings = getTodayBookings();
        long lastMonthBookings = getLastMonthBookings();
        if (lastMonthBookings == 0) {
            return currentBookings > 0 ? 100.0 : 0.0;
        }
        return ((currentBookings - lastMonthBookings) * 100.0) / lastMonthBookings;
    }
    
    public long getActiveFieldsCount() {
        return sanBongRepository.countByTrangThai("Hoạt động");
    }
    
    public long getTotalFieldsCount() {
        return sanBongRepository.count();
    }
    
    public double getFieldUtilizationRate() {
        long totalFields = getTotalFieldsCount();
        if (totalFields == 0) return 0.0;
        long activeFields = getActiveFields();
        return (activeFields * 100.0) / totalFields;
    }
    
    public long getNewCustomersThisWeek() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime startOfWeek = now.minusDays(now.getDayOfWeek().getValue() - 1).with(LocalTime.MIN);
        // Note: Assuming KhachHang has createdDate field - adjust based on actual model
        return khachHangRepository.findAll().stream()
            .filter(k -> {
                // You may need to check if KhachHang has creation date field
                // For now, return active customers this week
                return "Active".equals(k.getTrangThai()) && 
                       (k.getIsDeleted() == null || !k.getIsDeleted());
            })
            .count();
    }
    
    public long getNewCustomersLastWeek() {
        // Similar to above but for last week
        return 0; // Placeholder - implement based on your data structure
    }
    
    public double getCustomerChangePercentage() {
        long current = getNewCustomersThisWeek();
        long last = getNewCustomersLastWeek();
        if (last == 0) {
            return current > 0 ? 100.0 : 0.0;
        }
        return ((current - last) * 100.0) / last;
    }

    // =============== FIELD MANAGEMENT ===============
    
    public List<SanBong> getAllFields() {
        return sanBongRepository.findAll();
    }
    
    public List<NhanVien> getAllEmployees() {
        return nhanVienRepository.findAll();
    }
    
    public SanBong getFieldById(Integer id) {
        return sanBongRepository.findById(id).orElse(null);
    }
    
    public SanBong saveField(SanBong field) {
        return sanBongRepository.save(field);
    }
    
    public void deleteField(Integer id) {
        sanBongRepository.deleteById(id);
    }
    
    // Thêm sân mới với tính năng
    public SanBong addField(SanBong field, List<Integer> featureIds) {
        // Lưu sân
        field.setNgayTao(LocalDateTime.now());
        field.setNgayCapNhat(LocalDateTime.now());
        if (field.getTrangThai() == null || field.getTrangThai().isEmpty()) {
            field.setTrangThai("Hoạt động");
        }
        SanBong savedField = sanBongRepository.save(field);
        
        // Thêm tính năng nếu có
        if (featureIds != null && !featureIds.isEmpty()) {
            for (Integer featureId : featureIds) {
                Optional<vn.ute.utescore.model.TinhNang> tinhNangOpt = tinhNangRepository.findById(featureId);
                if (tinhNangOpt.isPresent()) {
                    TinhNangSan fieldFeature = new TinhNangSan();
                    fieldFeature.setId(new vn.ute.utescore.model.TinhNangSanId());
                    fieldFeature.getId().setMaSan(savedField.getMaSan());
                    fieldFeature.getId().setMaTinhNang(featureId);
                    
                    fieldFeature.setTinhNang(tinhNangOpt.get());
                    fieldFeature.setSanBong(savedField);
                    
                    tinhNangSanRepository.save(fieldFeature);
                }
            }
        }
        
        // Ghi lịch sử thay đổi trạng thái
        LichSuTrangThaiSan history = new LichSuTrangThaiSan();
        history.setSanBong(savedField);
        history.setTrangThaiCu("");
        history.setTrangThaiMoi(savedField.getTrangThai());
        history.setGhiChu("Sân mới được tạo");
        history.setThoiGianThayDoi(LocalDateTime.now());
        lichSuTrangThaiSanRepository.save(history);
        
        return savedField;
    }
    
    public void changeFieldStatus(Integer fieldId, String newStatus, String reason) {
        Optional<SanBong> fieldOpt = sanBongRepository.findById(fieldId);
        if (fieldOpt.isPresent()) {
            SanBong field = fieldOpt.get();
            String oldStatus = field.getTrangThai();
            
            // Cập nhật trạng thái sân
            field.setTrangThai(newStatus);
            field.setNgayCapNhat(LocalDateTime.now());
            sanBongRepository.save(field);
            
            // Ghi lịch sử thay đổi trạng thái
            LichSuTrangThaiSan history = new LichSuTrangThaiSan();
            history.setSanBong(field);
            history.setTrangThaiCu(oldStatus);
            history.setTrangThaiMoi(newStatus);
            history.setGhiChu(reason);
            history.setThoiGianThayDoi(LocalDateTime.now());
            
            lichSuTrangThaiSanRepository.save(history);
        }
    }

    // =============== MAINTENANCE MANAGEMENT ===============
    
    public boolean checkMaintenanceConflict(BaoTri maintenance) {
        if (maintenance.getSanBong() == null || maintenance.getNgayBaoTri() == null) {
            return false;
        }
        
        // Kiểm tra xung đột với lịch thuê hiện có
        // Convert LocalTime to String for native query
        String startTimeStr = maintenance.getThoiGianBatDau() != null ? maintenance.getThoiGianBatDau().toString() : null;
        String endTimeStr = maintenance.getThoiGianKetThuc() != null ? maintenance.getThoiGianKetThuc().toString() : null;
        
        List<ThueSan> conflictingBookings = thueSanRepository.findConflictingBookings(
            maintenance.getSanBong().getMaSan(),
            maintenance.getNgayBaoTri().toLocalDate(),
            startTimeStr,
            endTimeStr
        );
        
        return !conflictingBookings.isEmpty();
    }
    
    // Kiểm tra chi tiết xung đột
    public java.util.Map<String, Object> checkMaintenanceConflictDetails(BaoTri maintenance) {
        java.util.Map<String, Object> result = new java.util.HashMap<>();
        
        if (maintenance.getSanBong() == null || maintenance.getNgayBaoTri() == null) {
            result.put("hasConflict", false);
            result.put("message", "Dữ liệu không hợp lệ");
            return result;
        }
        
        // Convert LocalTime to String for native query
        String startTimeStr = maintenance.getThoiGianBatDau() != null ? maintenance.getThoiGianBatDau().toString() : null;
        String endTimeStr = maintenance.getThoiGianKetThuc() != null ? maintenance.getThoiGianKetThuc().toString() : null;
        
        List<ThueSan> conflictingBookings = thueSanRepository.findConflictingBookings(
            maintenance.getSanBong().getMaSan(),
            maintenance.getNgayBaoTri().toLocalDate(),
            startTimeStr,
            endTimeStr
        );
        
        result.put("hasConflict", !conflictingBookings.isEmpty());
        result.put("conflictingBookings", conflictingBookings);
        result.put("count", conflictingBookings.size());
        
        if (!conflictingBookings.isEmpty()) {
            result.put("message", "Có " + conflictingBookings.size() + " đặt lịch thuê trùng với thời gian bảo trì");
        } else {
            result.put("message", "Không có xung đột với lịch thuê");
        }
        
        return result;
    }
    
    public BaoTri scheduleMaintenance(BaoTri maintenance) {
        // Tự động chuyển trạng thái sân sang "Bảo trì"
        SanBong field = maintenance.getSanBong();
        field.setTrangThai("Bảo trì");
        field.setNgayCapNhat(LocalDateTime.now());
        sanBongRepository.save(field);
        
        // Lưu lịch bảo trì
        return baoTriRepository.save(maintenance);
    }
    
    public List<BaoTri> getMaintenanceList(String fieldId, String status) {
        if (fieldId != null && !fieldId.isEmpty()) {
            Integer id = Integer.valueOf(fieldId);
            if (status != null && !status.isEmpty()) {
                return baoTriRepository.findBySanBong_MaSanAndTrangThai(id, status);
            }
            return baoTriRepository.findBySanBong_MaSan(id);
        }
        
        if (status != null && !status.isEmpty()) {
            return baoTriRepository.findByTrangThai(status);
        }
        
        return baoTriRepository.findAll();
    }
    
    public List<MaintenanceDTO> getMaintenanceListDTO(String fieldId, String status) {
        List<BaoTri> list = getMaintenanceList(fieldId, status);
        return list.stream().map(m -> convertToDTO(m)).collect(Collectors.toList());
    }
    
    // Lấy danh sách với trạng thái động và lọc
    public List<MaintenanceDTO> getMaintenanceListDTOWithDynamicStatus(String fieldId, String status) {
        List<BaoTri> list;
        
        if (fieldId != null && !fieldId.isEmpty()) {
            Integer id = Integer.valueOf(fieldId);
            list = baoTriRepository.findBySanBong_MaSan(id);
        } else {
            list = baoTriRepository.findAll();
        }
        
        List<MaintenanceDTO> dtoList = list.stream().map(m -> convertToDTO(m)).collect(Collectors.toList());
        
        // Lọc theo trạng thái động nếu có
        if (status != null && !status.isEmpty()) {
            return dtoList.stream()
                .filter(dto -> status.equals(dto.getTrangThai()))
                .collect(Collectors.toList());
        }
        
        return dtoList;
    }
    
    public void updateMaintenanceStatus(Integer maintenanceId, String status) {
        Optional<BaoTri> maintenanceOpt = baoTriRepository.findById(maintenanceId);
        if (maintenanceOpt.isPresent()) {
            BaoTri maintenance = maintenanceOpt.get();
            maintenance.setTrangThai(status);
            
            // Nếu hoàn thành bảo trì, chuyển sân về trạng thái "Hoạt động"
            if ("Hoàn thành".equals(status)) {
                SanBong field = maintenance.getSanBong();
                field.setTrangThai("Hoạt động");
                field.setNgayCapNhat(LocalDateTime.now());
                sanBongRepository.save(field);
            }
            
            baoTriRepository.save(maintenance);
        }
    }
    
    public void deleteMaintenance(Integer maintenanceId) {
        Optional<BaoTri> maintenanceOpt = baoTriRepository.findById(maintenanceId);
        if (maintenanceOpt.isPresent()) {
            BaoTri maintenance = maintenanceOpt.get();
            // Nếu lịch bảo trì đang "Bảo trì", chuyển sân về "Hoạt động"
            if ("Bảo trì".equals(maintenance.getTrangThai())) {
                SanBong field = maintenance.getSanBong();
                if (field != null && "Bảo trì".equals(field.getTrangThai())) {
                    field.setTrangThai("Hoạt động");
                    field.setNgayCapNhat(LocalDateTime.now());
                    sanBongRepository.save(field);
                }
            }
            baoTriRepository.deleteById(maintenanceId);
        }
    }
    
    // Hủy lịch bảo trì (không xóa, chỉ đổi trạng thái)
    public void cancelMaintenance(Integer maintenanceId) {
        Optional<BaoTri> maintenanceOpt = baoTriRepository.findById(maintenanceId);
        if (maintenanceOpt.isPresent()) {
            BaoTri maintenance = maintenanceOpt.get();
            maintenance.setTrangThai("Đã hủy");
            baoTriRepository.save(maintenance);
            
            // Nếu sân đang ở trạng thái "Bảo trì", đổi về "Hoạt động"
            SanBong field = maintenance.getSanBong();
            if (field != null && "Bảo trì".equals(field.getTrangThai())) {
                field.setTrangThai("Hoạt động");
                field.setNgayCapNhat(LocalDateTime.now());
                sanBongRepository.save(field);
            }
        }
    }

    // =============== FIELD SEARCH AND FILTER ===============
    
    public List<SanBong> searchFields(String keyword, String status, String type, String area) {
        List<SanBong> results;
        
        // Áp dụng các bộ lọc
        if (keyword != null && !keyword.isEmpty()) {
            results = sanBongRepository.findByTenSanContainingIgnoreCase(keyword);
        } else {
            results = sanBongRepository.findAll();
        }
        
        // Lọc theo trạng thái
        if (status != null && !status.isEmpty()) {
            results = results.stream()
                .filter(s -> s.getTrangThai() != null && s.getTrangThai().equals(status))
                .collect(java.util.stream.Collectors.toList());
        }
        
        // Lọc theo loại sân
        if (type != null && !type.isEmpty()) {
            results = results.stream()
                .filter(s -> s.getLoaiSan() != null && s.getLoaiSan().equals(type))
                .collect(java.util.stream.Collectors.toList());
        }
        
        // Lọc theo khu vực
        if (area != null && !area.isEmpty()) {
            results = results.stream()
                .filter(s -> s.getKhuVuc() != null && s.getKhuVuc().toLowerCase().contains(area.toLowerCase()))
                .collect(java.util.stream.Collectors.toList());
        }
        
        return results;
    }
    
    // Tìm kiếm sân nâng cao với nhiều tiêu chí
    public List<SanBong> searchFieldsAdvanced(String keyword, String status, String type, 
                                               String area, List<Integer> featureIds, 
                                               Integer nhanVienId, String sortBy) {
        List<SanBong> results = sanBongRepository.findAll();
        
        // Lọc theo từ khóa
        if (keyword != null && !keyword.isEmpty()) {
            results = results.stream()
                .filter(s -> (s.getTenSan() != null && s.getTenSan().toLowerCase().contains(keyword.toLowerCase())) ||
                            (s.getMoTa() != null && s.getMoTa().toLowerCase().contains(keyword.toLowerCase())))
                .collect(java.util.stream.Collectors.toList());
        }
        
        // Lọc theo trạng thái
        if (status != null && !status.isEmpty()) {
            results = results.stream()
                .filter(s -> s.getTrangThai() != null && s.getTrangThai().equals(status))
                .collect(java.util.stream.Collectors.toList());
        }
        
        // Lọc theo loại sân
        if (type != null && !type.isEmpty()) {
            results = results.stream()
                .filter(s -> s.getLoaiSan() != null && s.getLoaiSan().equals(type))
                .collect(java.util.stream.Collectors.toList());
        }
        
        // Lọc theo khu vực
        if (area != null && !area.isEmpty()) {
            results = results.stream()
                .filter(s -> s.getKhuVuc() != null && s.getKhuVuc().toLowerCase().contains(area.toLowerCase()))
                .collect(java.util.stream.Collectors.toList());
        }
        
        // Lọc theo tính năng
        if (featureIds != null && !featureIds.isEmpty()) {
            for (Integer featureId : featureIds) {
                List<SanBong> fieldsWithFeature = tinhNangSanRepository.findByTinhNang_MaTinhNang(featureId)
                    .stream()
                    .map(TinhNangSan::getSanBong)
                    .collect(java.util.stream.Collectors.toList());
                
                results = results.stream()
                    .filter(fieldsWithFeature::contains)
                    .collect(java.util.stream.Collectors.toList());
            }
        }
        
        // Lọc theo nhân viên phụ trách
        if (nhanVienId != null) {
            results = results.stream()
                .filter(s -> {
                    List<Camera> cameras = s.getCameras();
                    if (cameras != null && !cameras.isEmpty()) {
                        return cameras.stream()
                            .anyMatch(c -> c.getNhanVienPhuTrach() != null && 
                                          c.getNhanVienPhuTrach().getUserID().equals(nhanVienId));
                    }
                    return false;
                })
                .collect(java.util.stream.Collectors.toList());
        }
        
        // Sắp xếp
        if (sortBy != null && !sortBy.isEmpty()) {
            switch(sortBy.toLowerCase()) {
                case "name":
                    results.sort((s1, s2) -> s1.getTenSan().compareToIgnoreCase(s2.getTenSan()));
                    break;
                case "status":
                    results.sort((s1, s2) -> {
                        String status1 = s1.getTrangThai() != null ? s1.getTrangThai() : "";
                        String status2 = s2.getTrangThai() != null ? s2.getTrangThai() : "";
                        return status1.compareTo(status2);
                    });
                    break;
                case "area":
                    results.sort((s1, s2) -> {
                        String area1 = s1.getKhuVuc() != null ? s1.getKhuVuc() : "";
                        String area2 = s2.getKhuVuc() != null ? s2.getKhuVuc() : "";
                        return area1.compareToIgnoreCase(area2);
                    });
                    break;
            }
        }
        
        return results;
    }

    // =============== PRICING MANAGEMENT ===============
    
    public List<GiaThue> getAllPricing() {
        return giaThueRepository.findAll();
    }
    
    public GiaThue savePricing(GiaThue pricing) {
        return giaThueRepository.save(pricing);
    }
    
    public List<GiaThue> getPricingByFieldType(String fieldType) {
        return giaThueRepository.findByLoaiSan(fieldType);
    }

    // =============== FIELD FEATURES MANAGEMENT ===============
    
    public List<TinhNangSan> getFieldFeatures(Integer fieldId) {
        return tinhNangSanRepository.findBySanBong_MaSan(fieldId);
    }
    
    public void updateFieldFeatures(Integer fieldId, List<TinhNangSan> features) {
        // Xóa các tính năng cũ
        tinhNangSanRepository.deleteBySanBong_MaSan(fieldId);
        
        // Thêm các tính năng mới
        SanBong field = sanBongRepository.findById(fieldId).orElse(null);
        if (field != null) {
            for (TinhNangSan feature : features) {
                feature.setSanBong(field);
                tinhNangSanRepository.save(feature);
            }
        }
    }

    // =============== FIELD OPERATIONS ===============
    
    public void openField(Integer fieldId, String reason) {
        changeFieldStatus(fieldId, "Hoạt động", reason);
    }
    
    public void closeField(Integer fieldId, String reason) {
        changeFieldStatus(fieldId, "Tạm đóng", reason);
    }
    
    public void permanentlyCloseField(Integer fieldId, String reason) {
        changeFieldStatus(fieldId, "Đóng vĩnh viễn", reason);
    }
    
    // =============== SUPPORT METHODS ===============
    
    public List<TinhNang> getAllFeatures() {
        return tinhNangRepository.findAll();
    }
    
    public List<SanBong> getFieldsByStatus(String status) {
        return sanBongRepository.findByTrangThai(status);
    }
    
    public List<SanBong> getFieldsByType(String type) {
        return sanBongRepository.findByLoaiSan(type);
    }
    
    public List<String> getAllFieldTypes() {
        return sanBongRepository.findAll()
            .stream()
            .map(SanBong::getLoaiSan)
            .filter(type -> type != null && !type.isEmpty())
            .distinct()
            .collect(java.util.stream.Collectors.toList());
    }
    
    public List<String> getAllAreas() {
        return sanBongRepository.findAll()
            .stream()
            .map(SanBong::getKhuVuc)
            .filter(area -> area != null && !area.isEmpty())
            .distinct()
            .collect(java.util.stream.Collectors.toList());
    }
    
    // Kiểm tra xem có đặt lịch thuê nào trùng với thời gian bảo trì không
    public boolean hasBookingConflict(Integer fieldId, LocalDateTime maintenanceDate, 
                                      java.time.LocalTime startTime, java.time.LocalTime endTime) {
        String startTimeStr = startTime != null ? startTime.toString() : null;
        String endTimeStr = endTime != null ? endTime.toString() : null;
        List<ThueSan> conflictingBookings = thueSanRepository.findConflictingBookings(
            fieldId,
            maintenanceDate.toLocalDate(),
            startTimeStr,
            endTimeStr
        );
        return !conflictingBookings.isEmpty();
    }
    
    // Lấy dữ liệu báo cáo bảo trì theo tháng
    public List<MaintenanceDTO> getMaintenanceReportByMonth(int year, int month) {
        LocalDate startDate = LocalDate.of(year, month, 1);
        LocalDate endDate = startDate.withDayOfMonth(startDate.lengthOfMonth());
        return getMaintenanceListDTO(startDate, endDate);
    }
    
    // Lấy dữ liệu báo cáo bảo trì theo quý
    public List<MaintenanceDTO> getMaintenanceReportByQuarter(int year, int quarter) {
        int startMonth = (quarter - 1) * 3 + 1;
        int endMonth = startMonth + 2;
        LocalDate startDate = LocalDate.of(year, startMonth, 1);
        LocalDate endDate = LocalDate.of(year, endMonth, 1).withDayOfMonth(
            LocalDate.of(year, endMonth, 1).lengthOfMonth()
        );
        return getMaintenanceListDTO(startDate, endDate);
    }
    
    // Lấy dữ liệu báo cáo bảo trì theo năm
    public List<MaintenanceDTO> getMaintenanceReportByYear(int year) {
        LocalDate startDate = LocalDate.of(year, 1, 1);
        LocalDate endDate = LocalDate.of(year, 12, 31);
        return getMaintenanceListDTO(startDate, endDate);
    }
    
    // Helper method để lấy dữ liệu bảo trì trong khoảng thời gian
    private List<MaintenanceDTO> getMaintenanceListDTO(LocalDate startDate, LocalDate endDate) {
        System.out.println("=== getMaintenanceListDTO: startDate=" + startDate + ", endDate=" + endDate);
        
        // Chuyển đổi LocalDate thành LocalDateTime để so sánh
        LocalDateTime startDateTime = startDate.atStartOfDay();
        LocalDateTime endDateTime = endDate.atTime(23, 59, 59);
        
        // Tìm kiếm bảo trì trong khoảng thời gian
        List<BaoTri> list = baoTriRepository.findByNgayBaoTriBetween(startDateTime, endDateTime);
        System.out.println("=== Found " + list.size() + " BaoTri records from database");
        
        List<MaintenanceDTO> dtoList = list.stream().map(m -> {
            MaintenanceDTO dto = convertToDTO(m);
            return dto;
        }).collect(Collectors.toList());
        System.out.println("=== Converted to " + dtoList.size() + " DTOs");
        return dtoList;
    }
    
    // Tính trạng thái động dựa trên thời gian
    private String calculateDynamicStatus(BaoTri maintenance) {
        // Nếu trạng thái là "Đã hủy", giữ nguyên
        if ("Đã hủy".equals(maintenance.getTrangThai())) {
            return "Đã hủy";
        }
        
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime maintenanceDate = maintenance.getNgayBaoTri();
        LocalTime startTime = maintenance.getThoiGianBatDau();
        LocalTime endTime = maintenance.getThoiGianKetThuc();
        
        // Kết hợp ngày và giờ để so sánh
        LocalDateTime startDateTime = LocalDateTime.of(maintenanceDate.toLocalDate(), startTime != null ? startTime : LocalTime.MIN);
        LocalDateTime endDateTime = LocalDateTime.of(maintenanceDate.toLocalDate(), endTime != null ? endTime : LocalTime.MAX);
        
        // So sánh với thời gian hiện tại
        if (now.isBefore(startDateTime)) {
            return "Đã lên lịch";
        } else if (now.isAfter(endDateTime)) {
            return "Hoàn thành";
        } else {
            return "Đang bảo trì";
        }
    }
    
    // Convert BaoTri to MaintenanceDTO với trạng thái động
    private MaintenanceDTO convertToDTO(BaoTri m) {
        MaintenanceDTO dto = new MaintenanceDTO();
        dto.setBaoTriID(m.getBaoTriID());
        dto.setTenSan(m.getSanBong() != null ? m.getSanBong().getTenSan() : "");
        dto.setLoaiSan(m.getSanBong() != null ? m.getSanBong().getLoaiSan() : "");
        dto.setThoiGianBatDau(m.getThoiGianBatDau());
        dto.setThoiGianKetThuc(m.getThoiGianKetThuc());
        dto.setNgayBaoTri(m.getNgayBaoTri());
        dto.setFullName(m.getNhanVien() != null ? m.getNhanVien().getFullName() : "");
        // Sử dụng trạng thái động
        dto.setTrangThai(calculateDynamicStatus(m));
        dto.setLyDo(m.getLyDo());
        return dto;
    }
    
    // =============== USER MANAGEMENT ===============
    
    // Lấy tất cả khách hàng
    public List<KhachHang> getAllCustomers() {
        return khachHangRepository.findNotDeleted();
    }
    
    // Lấy danh sách khách hàng đã xóa
    public List<KhachHang> getDeletedCustomers() {
        return khachHangRepository.findDeleted();
    }
    
    // Lấy tất cả nhân viên
    public List<NhanVien> getAllStaff() {
        return nhanVienRepository.findAll();
    }
    
    // Get all employees as DTOs to avoid circular reference
    public List<vn.ute.utescore.dto.EmployeeDTO> getAllStaffAsDTO() {
        List<NhanVien> employees = nhanVienRepository.findAll();
        List<vn.ute.utescore.dto.EmployeeDTO> employeeDTOs = new java.util.ArrayList<>();
        
        for (NhanVien employee : employees) {
            vn.ute.utescore.dto.EmployeeDTO dto = new vn.ute.utescore.dto.EmployeeDTO(
                employee.getUserID(),
                employee.getFullName(),
                employee.getEmail(),
                employee.getPhone(),
                employee.getStatus(),
                employee.getCreatedAt(),
                employee.getUpdateAt(),
                employee.getRole() != null ? employee.getRole().getRoleID() : null,
                employee.getRole() != null ? employee.getRole().getRoleName() : null
            );
            employeeDTOs.add(dto);
        }
        
        return employeeDTOs;
    }
    
    // Thêm nhân viên mới
    public NhanVien addEmployee(NhanVien employee, String password, boolean sendEmail) {
        // Kiểm tra email trùng lặp
        if (nhanVienRepository.findByEmail(employee.getEmail()).isPresent()) {
            throw new RuntimeException("Email đã được sử dụng");
        }
        
        // Kiểm tra số điện thoại trùng lặp
        if (nhanVienRepository.findByPhone(employee.getPhone()).isPresent()) {
            throw new RuntimeException("Số điện thoại đã được sử dụng");
        }
        
        // Normalize status: convert "Active"/"Inactive" to "1"/"0" for database
        String status = employee.getStatus();
        if ("Active".equalsIgnoreCase(status)) {
            status = "1";
        } else if ("Inactive".equalsIgnoreCase(status) || "0".equals(status)) {
            status = "0";
        }
        employee.setStatus(status);
        
        employee.setCreatedAt(LocalDateTime.now());
        employee.setUpdateAt(LocalDateTime.now());
        
        // Use provided password or generate a random one
        String finalPassword = (password != null && !password.trim().isEmpty()) 
            ? password 
            : generateRandomPassword();
        
        // Tạo tài khoản cho nhân viên
        TaiKhoan taiKhoan = new TaiKhoan();
        taiKhoan.setEmail(employee.getEmail());
        taiKhoan.setSoDienThoai(employee.getPhone());
        // LƯU Ý: luôn mã hoá mật khẩu trước khi lưu DB để đồng nhất với luồng đăng nhập guest
        taiKhoan.setMatKhau(passwordEncoder.encode(finalPassword));
        taiKhoan.setRole(employee.getRole());
        
        // Set status to "1" (Active) or "0" (Blocked) based on employee status
        String dbStatus = "1".equals(employee.getStatus()) || "Active".equalsIgnoreCase(employee.getStatus()) ? "1" : "0";
        taiKhoan.setTrangThai(dbStatus);
        
        taiKhoanRepository.save(taiKhoan);
        
        // Save employee
        NhanVien savedEmployee = nhanVienRepository.save(employee);
        
        // Gửi email thông báo tài khoản đã được tạo (chỉ khi sendEmail = true)
        if (sendEmail) {
            try {
                emailService.sendAccountCreationEmail(
                    employee.getEmail(), 
                    employee.getFullName(), 
                    employee.getRole().getRoleName(),
                    finalPassword // Send the actual password
                );
                System.out.println("✅ Email đã được gửi đến: " + employee.getEmail());
            } catch (Exception e) {
                System.err.println("❌ Lỗi gửi email: " + e.getMessage());
                // Không throw exception để không làm gián đoạn quá trình tạo tài khoản
            }
        } else {
            System.out.println("⚠️ Email notification disabled by user");
        }
        
        return savedEmployee;
    }
    
    // Lấy danh sách vai trò
    public List<Roles> getAllRoles() {
        return rolesRepository.findAllOrdered();
    }
    
    // Get all roles as DTOs to avoid circular reference
    public List<vn.ute.utescore.dto.RoleDTO> getAllRolesAsDTO() {
        List<Roles> roles = rolesRepository.findAllOrdered();
        List<vn.ute.utescore.dto.RoleDTO> roleDTOs = new java.util.ArrayList<>();
        
        for (Roles role : roles) {
            vn.ute.utescore.dto.RoleDTO dto = new vn.ute.utescore.dto.RoleDTO(
                role.getRoleID(),
                role.getRoleName(),
                role.getMoTa()
            );
            roleDTOs.add(dto);
        }
        
        return roleDTOs;
    }
    
    // Tìm kiếm khách hàng
    public List<KhachHang> searchCustomers(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return khachHangRepository.findNotDeleted();
        }
        return khachHangRepository.searchCustomers(keyword);
    }
    
    // Tìm kiếm nhân viên
    public List<NhanVien> searchEmployees(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return nhanVienRepository.findAll();
        }
        return nhanVienRepository.findByFullNameContainingIgnoreCase(keyword);
    }
    
    // Cập nhật trạng thái nhân viên
    public NhanVien updateEmployeeStatus(Integer employeeId, String status) {
        NhanVien employee = nhanVienRepository.findById(employeeId)
            .orElseThrow(() -> new RuntimeException("Không tìm thấy nhân viên"));
        
        // Normalize status: convert "Active"/"Inactive" to "1"/"0" for database
        final String dbStatus;
        if ("Active".equalsIgnoreCase(status)) {
            dbStatus = "1";
        } else if ("Inactive".equalsIgnoreCase(status) || "0".equals(status)) {
            dbStatus = "0";
        } else {
            dbStatus = status; // Keep as-is if not recognized
        }
        
        employee.setStatus(dbStatus);
        employee.setUpdateAt(LocalDateTime.now());
        
        // Cập nhật trạng thái tài khoản
        final String finalDbStatus = dbStatus;
        taiKhoanRepository.findById(employee.getEmail()).ifPresent(taiKhoan -> {
            taiKhoan.setTrangThai(finalDbStatus);
            taiKhoanRepository.save(taiKhoan);
        });
        
        return nhanVienRepository.save(employee);
    }
    
    // Reset mật khẩu
    public String resetPassword(String email) {
        System.out.println("=== Searching for TaiKhoan with email: " + email);
        
        // Try to find existing TaiKhoan
        TaiKhoan taiKhoan = taiKhoanRepository.findById(email).orElse(null);
        
        // If not found, try to create one from NhanVien
        if (taiKhoan == null) {
            System.out.println("=== TaiKhoan not found, trying to find NhanVien...");
            NhanVien employee = nhanVienRepository.findByEmail(email).orElse(null);
            
            if (employee != null) {
                System.out.println("=== Found NhanVien, creating new TaiKhoan...");
                // Create a new TaiKhoan for this employee
                taiKhoan = new TaiKhoan();
                taiKhoan.setEmail(employee.getEmail());
                taiKhoan.setSoDienThoai(employee.getPhone());
                taiKhoan.setMatKhau(generateRandomPassword()); // Temporary password
                taiKhoan.setRole(employee.getRole());
                taiKhoan.setTrangThai(employee.getStatus());
                
                taiKhoanRepository.save(taiKhoan);
                System.out.println("=== Created new TaiKhoan for: " + employee.getEmail());
            } else {
                System.err.println("ERROR: Neither TaiKhoan nor NhanVien found for email: " + email);
                throw new RuntimeException("Không tìm thấy tài khoản với email: " + email);
            }
        } else {
            System.out.println("=== Found existing TaiKhoan: " + taiKhoan.getEmail());
        }
        
        // Tạo mật khẩu mới ngẫu nhiên (gửi plain qua email, lưu DB dưới dạng mã hoá)
        String newPassword = generateRandomPassword();
        taiKhoan.setMatKhau(passwordEncoder.encode(newPassword));
        
        taiKhoanRepository.save(taiKhoan);
        
        // Lấy thông tin nhân viên để gửi email
        NhanVien employee = nhanVienRepository.findByEmail(email).orElse(null);
        String userName = employee != null ? employee.getFullName() : email;
        
        // Gửi email thông báo mật khẩu mới
        try {
            emailService.sendPasswordResetEmail(email, newPassword, userName);
        } catch (Exception e) {
            System.err.println("Lỗi gửi email: " + e.getMessage());
            // Không throw exception để không làm gián đoạn quá trình reset password
        }
        
        System.out.println("=========================================");
        System.out.println("✅ RESET PASSWORD SUCCESSFUL");
        System.out.println("=========================================");
        System.out.println("Email: " + email);
        System.out.println("New Password: " + newPassword);
        System.out.println("Email notification sent to: " + email);
        System.out.println("=========================================");
        
        return newPassword;
    }
    
    // Khôi phục tài khoản khách hàng
    public KhachHang recoverCustomer(Integer customerId) {
        KhachHang customer = khachHangRepository.findById(customerId)
            .orElseThrow(() -> new RuntimeException("Không tìm thấy khách hàng"));
        
        customer.setIsDeleted(false);
        customer.setTrangThai("Active");
        
        return khachHangRepository.save(customer);
    }
    
    // Khôi phục tài khoản nhân viên
    public NhanVien recoverEmployee(Integer employeeId) {
        NhanVien employee = nhanVienRepository.findById(employeeId)
            .orElseThrow(() -> new RuntimeException("Không tìm thấy nhân viên"));
        
        employee.setStatus("1"); // 1 = Active
        employee.setUpdateAt(LocalDateTime.now());
        
        // Cập nhật trạng thái tài khoản
        taiKhoanRepository.findById(employee.getEmail()).ifPresent(taiKhoan -> {
            taiKhoan.setTrangThai("1");
            taiKhoanRepository.save(taiKhoan);
        });
        
        return nhanVienRepository.save(employee);
    }
    
    // Lấy tất cả tài khoản bị khóa (khách hàng đã xóa + nhân viên bị khóa)
    public List<vn.ute.utescore.dto.UserDTO> getAllLockedUsersAsDTO() {
        List<vn.ute.utescore.dto.UserDTO> lockedUsers = new java.util.ArrayList<>();
        
        // Add deleted customers
        List<KhachHang> deletedCustomers = khachHangRepository.findDeleted();
        for (KhachHang customer : deletedCustomers) {
            vn.ute.utescore.dto.UserDTO dto = new vn.ute.utescore.dto.UserDTO(
                customer.getMaKhachHang(),
                customer.getHoTen(),
                customer.getEmail(),
                customer.getSoDienThoai(),
                "Khách hàng",
                customer.getTrangThai(),
                customer.getNgayDangKy(),
                "customer"
            );
            lockedUsers.add(dto);
        }
        
        // Add inactive employees (status = "0")
        List<NhanVien> inactiveEmployees = nhanVienRepository.findByStatus("0");
        for (NhanVien employee : inactiveEmployees) {
            String roleName = employee.getRole() != null ? employee.getRole().getRoleName() : "N/A";
            vn.ute.utescore.dto.UserDTO dto = new vn.ute.utescore.dto.UserDTO(
                employee.getUserID(),
                employee.getFullName(),
                employee.getEmail(),
                employee.getPhone(),
                roleName,
                employee.getStatus(),
                employee.getCreatedAt(),
                "employee"
            );
            lockedUsers.add(dto);
        }
        
        return lockedUsers;
    }
    
    // Helper: Tạo mật khẩu ngẫu nhiên
    private String generateRandomPassword() {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*";
        StringBuilder password = new StringBuilder();
        for (int i = 0; i < 12; i++) {
            password.append(chars.charAt((int) (Math.random() * chars.length())));
        }
        return password.toString();
    }
    
    // Get all users as DTOs (to avoid circular reference)
    public List<vn.ute.utescore.dto.UserDTO> getAllUsersAsDTO() {
        List<vn.ute.utescore.dto.UserDTO> users = new java.util.ArrayList<>();
        
        // Add customers
        List<KhachHang> customers = getAllCustomers();
        for (KhachHang customer : customers) {
            vn.ute.utescore.dto.UserDTO dto = new vn.ute.utescore.dto.UserDTO(
                customer.getMaKhachHang(),
                customer.getHoTen(),
                customer.getEmail(),
                customer.getSoDienThoai(),
                "Khách hàng",
                customer.getTrangThai(),
                customer.getNgayDangKy(),
                "customer"
            );
            users.add(dto);
        }
        
        // Add employees
        List<NhanVien> employees = getAllStaff();
        for (NhanVien employee : employees) {
            String roleName = employee.getRole() != null ? employee.getRole().getRoleName() : "N/A";
            vn.ute.utescore.dto.UserDTO dto = new vn.ute.utescore.dto.UserDTO(
                employee.getUserID(),
                employee.getFullName(),
                employee.getEmail(),
                employee.getPhone(),
                roleName,
                employee.getStatus(),
                employee.getCreatedAt(),
                "employee"
            );
            users.add(dto);
        }
        
        return users;
    }
    
    // =============== REVENUE STATISTICS ===============
    
    /**
     * Get total revenue for today
     */
    public BigDecimal getTodayRevenue() {
        LocalDateTime startOfDay = LocalDateTime.now().with(LocalTime.MIN);
        LocalDateTime endOfDay = LocalDateTime.now().with(LocalTime.MAX);
        BigDecimal revenue = thanhToanRepository.calculateTotalRevenue(startOfDay, endOfDay);
        return revenue != null ? revenue : BigDecimal.ZERO;
    }
    
    /**
     * Get total revenue for this week
     */
    public BigDecimal getThisWeekRevenue() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime startOfWeek = now.minusDays(now.getDayOfWeek().getValue() - 1).with(LocalTime.MIN);
        LocalDateTime endOfWeek = now.with(LocalTime.MAX);
        BigDecimal revenue = thanhToanRepository.calculateTotalRevenue(startOfWeek, endOfWeek);
        return revenue != null ? revenue : BigDecimal.ZERO;
    }
    
    /**
     * Get total revenue for this month
     */
    public BigDecimal getThisMonthRevenue() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime startOfMonth = now.withDayOfMonth(1).with(LocalTime.MIN);
        LocalDateTime endOfMonth = now.withDayOfMonth(now.toLocalDate().lengthOfMonth()).with(LocalTime.MAX);
        BigDecimal revenue = thanhToanRepository.calculateTotalRevenue(startOfMonth, endOfMonth);
        return revenue != null ? revenue : BigDecimal.ZERO;
    }
    
    /**
     * Get total revenue for this year
     */
    public BigDecimal getThisYearRevenue() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime startOfYear = now.withDayOfYear(1).with(LocalTime.MIN);
        LocalDateTime endOfYear = now.withDayOfYear(now.toLocalDate().lengthOfYear()).with(LocalTime.MAX);
        BigDecimal revenue = thanhToanRepository.calculateTotalRevenue(startOfYear, endOfYear);
        return revenue != null ? revenue : BigDecimal.ZERO;
    }
    
    /**
     * Get revenue by custom date range
     */
    public BigDecimal getRevenueByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        BigDecimal revenue = thanhToanRepository.calculateTotalRevenue(startDate, endDate);
        return revenue != null ? revenue : BigDecimal.ZERO;
    }
    
    /**
     * Get previous day revenue for comparison
     */
    public BigDecimal getYesterdayRevenue() {
        LocalDateTime yesterday = LocalDateTime.now().minusDays(1);
        LocalDateTime startOfDay = yesterday.with(LocalTime.MIN);
        LocalDateTime endOfDay = yesterday.with(LocalTime.MAX);
        BigDecimal revenue = thanhToanRepository.calculateTotalRevenue(startOfDay, endOfDay);
        return revenue != null ? revenue : BigDecimal.ZERO;
    }
    
    /**
     * Get previous week revenue for comparison
     */
    public BigDecimal getLastWeekRevenue() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime startOfLastWeek = now.minusWeeks(1).minusDays(now.getDayOfWeek().getValue() - 1).with(LocalTime.MIN);
        LocalDateTime endOfLastWeek = now.minusWeeks(1).with(LocalTime.MAX);
        BigDecimal revenue = thanhToanRepository.calculateTotalRevenue(startOfLastWeek, endOfLastWeek);
        return revenue != null ? revenue : BigDecimal.ZERO;
    }
    
    /**
     * Get previous month revenue for comparison
     */
    public BigDecimal getLastMonthRevenue() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime lastMonth = now.minusMonths(1);
        LocalDateTime startOfMonth = lastMonth.withDayOfMonth(1).with(LocalTime.MIN);
        LocalDateTime endOfMonth = lastMonth.withDayOfMonth(lastMonth.toLocalDate().lengthOfMonth()).with(LocalTime.MAX);
        BigDecimal revenue = thanhToanRepository.calculateTotalRevenue(startOfMonth, endOfMonth);
        return revenue != null ? revenue : BigDecimal.ZERO;
    }
    
    /**
     * Get previous year revenue for comparison
     */
    public BigDecimal getLastYearRevenue() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime lastYear = now.minusYears(1);
        LocalDateTime startOfYear = lastYear.withDayOfYear(1).with(LocalTime.MIN);
        LocalDateTime endOfYear = lastYear.withDayOfYear(lastYear.toLocalDate().lengthOfYear()).with(LocalTime.MAX);
        BigDecimal revenue = thanhToanRepository.calculateTotalRevenue(startOfYear, endOfYear);
        return revenue != null ? revenue : BigDecimal.ZERO;
    }
    
    /**
     * Calculate percentage change between two amounts
     */
    public BigDecimal calculatePercentageChange(BigDecimal current, BigDecimal previous) {
        if (previous == null || previous.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO;
        }
        BigDecimal change = current.subtract(previous);
        return change.divide(previous, 4, java.math.RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(100));
    }
    
    /**
     * Get revenue statistics with comparison
     */
    public vn.ute.utescore.dto.RevenueDTO getRevenueStatistics(String period) {
        java.time.LocalDate today = java.time.LocalDate.now();
        BigDecimal currentRevenue = BigDecimal.ZERO;
        BigDecimal previousRevenue = BigDecimal.ZERO;
        
        switch (period.toLowerCase()) {
            case "day":
            case "today":
                currentRevenue = getTodayRevenue();
                previousRevenue = getYesterdayRevenue();
                break;
            case "week":
                currentRevenue = getThisWeekRevenue();
                previousRevenue = getLastWeekRevenue();
                break;
            case "month":
                currentRevenue = getThisMonthRevenue();
                previousRevenue = getLastMonthRevenue();
                break;
            case "year":
                currentRevenue = getThisYearRevenue();
                previousRevenue = getLastYearRevenue();
                break;
        }
        
        vn.ute.utescore.dto.RevenueDTO revenueDTO = new vn.ute.utescore.dto.RevenueDTO(today, currentRevenue, 0L);
        revenueDTO.setPreviousPeriodRevenue(previousRevenue);
        revenueDTO.setChangePercentage(calculatePercentageChange(currentRevenue, previousRevenue));
        
        return revenueDTO;
    }
    
    // =============== PAYMENT DTO ===============
    
    /**
     * Get all payments as DTOs with customer and field information
     */
    @org.springframework.transaction.annotation.Transactional(readOnly = true, propagation = org.springframework.transaction.annotation.Propagation.REQUIRES_NEW)
    public List<PaymentDTO> getAllPaymentsAsDTO() {
        System.out.println("===== Starting getAllPaymentsAsDTO() =====");
        try {
            // First try with simple query
            List<vn.ute.utescore.model.ThanhToan> payments = thanhToanRepository.findAll();
            System.out.println("Loaded " + payments.size() + " payments from database");
            
            if (payments == null || payments.isEmpty()) {
                System.out.println("No payments found in database!");
                return new java.util.ArrayList<>();
            }
            
            System.out.println("Processing first payment: " + payments.get(0).getMaThanhToan());
        List<PaymentDTO> paymentDTOs = new java.util.ArrayList<>();
        
        for (vn.ute.utescore.model.ThanhToan payment : payments) {
            try {
                PaymentDTO dto = new PaymentDTO();
                
                // Payment basic info - truy cập TRONG TRANSACTION
                dto.setMaThanhToan(payment.getMaThanhToan());
                dto.setSoTienNhan(payment.getSoTienNhan());
                dto.setPhuongThuc(payment.getPhuongThuc());
                dto.setLoaiThanhToan(payment.getLoaiThanhToan());
                dto.setTrangThaiThanhToan(payment.getTrangThaiThanhToan());
                dto.setSoDiemSuDung(payment.getSoDiemSuDung());
                dto.setGiaTriDiemGiam(payment.getGiaTriDiemGiam());
                dto.setSoDiemCongThem(payment.getSoDiemCongThem());
                dto.setNgayThanhToan(payment.getNgayThanhToan());
                dto.setMaGiaoDich(payment.getMaGiaoDich());
                dto.setGhiChu(payment.getGhiChu());
                
                // PHẢI truy cập relationship trong transaction để lazy load
                vn.ute.utescore.model.ThueSan thueSan = payment.getThueSan();
                
                if (thueSan != null) {
                    dto.setMaDonDat(thueSan.getMaDonDat());
                    
                    // Truy cập customer để trigger lazy load
                    vn.ute.utescore.model.KhachHang customer = thueSan.getKhachHang();
                    if (customer != null) {
                        dto.setMaKhachHang(customer.getMaKhachHang());
                        dto.setHoTen(customer.getHoTen());
                        dto.setSoDienThoai(customer.getSoDienThoai());
                        dto.setEmail(customer.getEmail());
                        System.out.println("Payment " + dto.getMaThanhToan() + " - Customer: " + dto.getHoTen());
                    }
                    
                    // Truy cập field để trigger lazy load
                    vn.ute.utescore.model.SanBong field = thueSan.getSanBong();
                    if (field != null) {
                        dto.setMaSan(field.getMaSan());
                        dto.setTenSan(field.getTenSan());
                        dto.setLoaiSan(field.getLoaiSan());
                        System.out.println("Payment " + dto.getMaThanhToan() + " - Field: " + dto.getTenSan());
                    }
                }
                
                paymentDTOs.add(dto);
            } catch (Exception e) {
                System.err.println("Error processing payment " + payment.getMaThanhToan() + ": " + e.getMessage());
                e.printStackTrace();
            }
        }
        
        System.out.println("===== Successfully created " + paymentDTOs.size() + " payment DTOs =====");
        if (!paymentDTOs.isEmpty()) {
            PaymentDTO firstDTO = paymentDTOs.get(0);
            System.out.println("Sample DTO - maThanhToan: " + firstDTO.getMaThanhToan() + 
                              ", hoTen: " + firstDTO.getHoTen() + 
                              ", tenSan: " + firstDTO.getTenSan());
        }
        return paymentDTOs;
        } catch (Exception e) {
            System.err.println("Error in getAllPaymentsAsDTO: " + e.getMessage());
            e.printStackTrace();
            return new java.util.ArrayList<>();
        }
    }
    
    // Get payment analytics (top field, staff, area, payment method)
    public java.util.Map<String, Object> getPaymentAnalytics() {
        System.out.println("===== Starting getPaymentAnalytics() =====");
        java.util.Map<String, Object> analytics = new java.util.HashMap<>();
        
        try {
            // Get all successful payments with relationships
            List<vn.ute.utescore.model.ThanhToan> allPayments = thanhToanRepository.findAll();
            
            if (allPayments == null || allPayments.isEmpty()) {
                // Return default values if no payments
                analytics.put("field", "Chưa có dữ liệu");
                analytics.put("fieldAmount", "₫0");
                analytics.put("staff", "Chưa có dữ liệu");
                analytics.put("staffCount", "0 giao dịch");
                analytics.put("area", "Chưa có dữ liệu");
                analytics.put("areaPercent", "0% tổng giao dịch");
                analytics.put("method", "Chưa có dữ liệu");
                analytics.put("methodPercent", "0% tổng giao dịch");
                return analytics;
            }
            
            // Calculate top field type by revenue (5/7/11)
            java.util.Map<String, java.math.BigDecimal> fieldTypeRevenueMap = new java.util.HashMap<>();
            
            // Calculate top area by bookings (not just transactions, but area with most field bookings)
            java.util.Map<String, Integer> areaBookingCount = new java.util.HashMap<>();
            
            // Calculate top payment method
            java.util.Map<String, Integer> methodCount = new java.util.HashMap<>();
            
            int totalTransactions = 0;
            
            for (vn.ute.utescore.model.ThanhToan payment : allPayments) {
                try {
                    vn.ute.utescore.model.ThueSan thueSan = payment.getThueSan();
                    if (thueSan != null) {
                        // Track field type by revenue (LoaiSan: 5/7/11)
                        vn.ute.utescore.model.SanBong field = thueSan.getSanBong();
                        if (field != null && payment.getSoTienNhan() != null) {
                            String fieldType = field.getLoaiSan(); // e.g., "Sân 5", "Sân 7", "Sân 11"
                            fieldTypeRevenueMap.put(fieldType, fieldTypeRevenueMap.getOrDefault(fieldType, java.math.BigDecimal.ZERO).add(payment.getSoTienNhan()));
                        }
                        
                        // Track area by bookings
                        if (field != null && field.getKhuVuc() != null) {
                            String area = field.getKhuVuc();
                            areaBookingCount.put(area, areaBookingCount.getOrDefault(area, 0) + 1);
                        }
                    }
                    
                    // Track payment method
                    if (payment.getPhuongThuc() != null) {
                        methodCount.put(payment.getPhuongThuc(), methodCount.getOrDefault(payment.getPhuongThuc(), 0) + 1);
                    }
                    
                    totalTransactions++;
                } catch (Exception e) {
                    System.err.println("Error processing payment for analytics: " + e.getMessage());
                }
            }
            
            // Find top field type by revenue
            String topFieldType = "Chưa có dữ liệu";
            String topFieldAmount = "₫0";
            if (!fieldTypeRevenueMap.isEmpty()) {
                topFieldType = fieldTypeRevenueMap.entrySet().stream()
                    .max(java.util.Map.Entry.comparingByValue())
                    .map(entry -> entry.getKey())
                    .orElse("Chưa có dữ liệu");
                
                java.math.BigDecimal maxRevenue = fieldTypeRevenueMap.get(topFieldType);
                topFieldAmount = "₫" + maxRevenue.divide(new java.math.BigDecimal("1000000"), 1, java.math.RoundingMode.HALF_UP) + "M";
            }
            
            // Find top area by bookings
            String topArea = "Chưa có dữ liệu";
            String topAreaPercent = "0% tổng giao dịch";
            if (!areaBookingCount.isEmpty()) {
                topArea = areaBookingCount.entrySet().stream()
                    .max(java.util.Map.Entry.comparingByValue())
                    .map(entry -> entry.getKey())
                    .orElse("Chưa có dữ liệu");
                
                int areaBookings = areaBookingCount.get(topArea);
                double percent = (areaBookings * 100.0) / totalTransactions;
                topAreaPercent = String.format("%.0f%% tổng giao dịch", percent);
            }
            
            // Find top payment method
            String topMethod = "Chưa có dữ liệu";
            String topMethodPercent = "0% tổng giao dịch";
            if (!methodCount.isEmpty()) {
                topMethod = methodCount.entrySet().stream()
                    .max(java.util.Map.Entry.comparingByValue())
                    .map(entry -> entry.getKey())
                    .orElse("Chưa có dữ liệu");
                
                int methodTransactions = methodCount.get(topMethod);
                double percent = (methodTransactions * 100.0) / totalTransactions;
                topMethodPercent = String.format("%.0f%% tổng giao dịch", percent);
            }
            
            analytics.put("field", topFieldType);
            analytics.put("fieldAmount", topFieldAmount);
            analytics.put("staff", "Nguyễn Văn A"); // TODO: Implement staff tracking
            analytics.put("staffCount", totalTransactions + " giao dịch");
            analytics.put("area", topArea);
            analytics.put("areaPercent", topAreaPercent);
            analytics.put("method", topMethod);
            analytics.put("methodPercent", topMethodPercent);
            
            System.out.println("Analytics calculated - Field Type: " + topFieldType + ", Area: " + topArea + ", Method: " + topMethod);
            return analytics;
        } catch (Exception e) {
            System.err.println("Error in getPaymentAnalytics: " + e.getMessage());
            e.printStackTrace();
            analytics.put("field", "Lỗi");
            analytics.put("fieldAmount", "₫0");
            analytics.put("staff", "Lỗi");
            analytics.put("staffCount", "0 giao dịch");
            analytics.put("area", "Lỗi");
            analytics.put("areaPercent", "0% tổng giao dịch");
            analytics.put("method", "Lỗi");
            analytics.put("methodPercent", "0% tổng giao dịch");
            return analytics;
        }
    }
    
    // =============== REFUND MANAGEMENT ===============
    
    public long getPendingRefundCount() {
        try {
            // Count refunds that are pending (Chờ duyệt)
            return hoanTienRepository.findPendingRefunds().size();
        } catch (Exception e) {
            System.err.println("Error getting pending refund count: " + e.getMessage());
            return 0;
        }
    }
    
    public long getCompletedRefundCount() {
        try {
            // Count refunds that are completed (Đã hoàn) - have been processed
            List<HoanTien> completed = hoanTienRepository.findCompletedRefunds();
            return completed.size();
        } catch (Exception e) {
            System.err.println("Error getting completed refund count: " + e.getMessage());
            return 0;
        }
    }
    
    public long getRejectedRefundCount() {
        try {
            // Count refunds that are rejected (Từ chối)
            return hoanTienRepository.findRejectedRefunds().size();
        } catch (Exception e) {
            System.err.println("Error getting rejected refund count: " + e.getMessage());
            return 0;
        }
    }
    
    public long getCompletedRefundCountToday() {
        try {
            List<HoanTien> completed = hoanTienRepository.findCompletedRefunds();
            LocalDateTime todayStart = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0);
            LocalDateTime todayEnd = LocalDateTime.now().withHour(23).withMinute(59).withSecond(59);
            
            return completed.stream()
                .filter(r -> r.getNgayXuLy() != null && 
                           r.getNgayXuLy().isAfter(todayStart) && 
                           r.getNgayXuLy().isBefore(todayEnd))
                .count();
        } catch (Exception e) {
            System.err.println("Error getting completed refund count today: " + e.getMessage());
            return 0;
        }
    }
    
    public BigDecimal getTotalRefundAmount() {
        try {
            // Get all refunds and filter by status in Java code
            List<HoanTien> allRefunds = hoanTienRepository.findAll();
            
            // Filter refunds with status 'Đã hoàn' in Java code to avoid encoding issues
            BigDecimal total = BigDecimal.ZERO;
            for (HoanTien refund : allRefunds) {
                String status = refund.getTrangThaiHoan();
                if (status != null && status.equals("Đã hoàn")) {
                    BigDecimal amount = refund.getSoTienHoan();
                    if (amount != null) {
                        total = total.add(amount);
                        System.out.println("Refund ID: " + refund.getMaHoanTien() + ", Status: '" + status + "', Amount: " + amount);
                    }
                }
            }
            
            System.out.println("===== Total refund amount: " + total + " =====");
            return total;
        } catch (Exception e) {
            System.err.println("Error calculating total refund amount: " + e.getMessage());
            e.printStackTrace();
            return BigDecimal.ZERO;
        }
    }
    
    public BigDecimal getTotalGrossRevenue() {
        try {
            // Get all successful payments
            List<ThanhToan> successfulPayments = thanhToanRepository.findSuccessfulPayments();
            return successfulPayments.stream()
                .map(ThanhToan::getSoTienNhan)
                .filter(java.util.Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        } catch (Exception e) {
            System.err.println("Error calculating total gross revenue: " + e.getMessage());
            return BigDecimal.ZERO;
        }
    }
    
    public BigDecimal calculateRevenueImpact() {
        try {
            // Get total gross revenue
            BigDecimal totalRevenue = getTotalGrossRevenue();
            
            // Get approved refunds
            BigDecimal totalRefund = getTotalRefundAmount();
            
            // Calculate impact (net revenue)
            return totalRevenue.subtract(totalRefund);
        } catch (Exception e) {
            System.err.println("Error calculating revenue impact: " + e.getMessage());
            return BigDecimal.ZERO;
        }
    }
    
    public List<HoanTien> getAllRefunds() {
        try {
            return hoanTienRepository.findAll();
        } catch (Exception e) {
            System.err.println("Error getting all refunds: " + e.getMessage());
            return new java.util.ArrayList<>();
        }
    }
    
    public void approveRefund(Integer id) {
        try {
            HoanTien refund = hoanTienRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Refund not found"));
            
            refund.setTrangThaiHoan("Đã hoàn");
            refund.setNgayXuLy(LocalDateTime.now());
            hoanTienRepository.save(refund);
        } catch (Exception e) {
            System.err.println("Error approving refund: " + e.getMessage());
            throw e;
        }
    }
    
    public void rejectRefund(Integer id, String reason) {
        try {
            HoanTien refund = hoanTienRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Refund not found"));
            
            refund.setTrangThaiHoan("Từ chối");
            refund.setNgayXuLy(LocalDateTime.now());
            refund.setGhiChu(reason);
            hoanTienRepository.save(refund);
        } catch (Exception e) {
            System.err.println("Error rejecting refund: " + e.getMessage());
            throw e;
        }
    }
    
    // Get refund trend data by period
    public java.util.Map<String, Object> getRefundTrendData(String period) {
        try {
            List<HoanTien> allRefunds = hoanTienRepository.findAll();
            LocalDateTime now = LocalDateTime.now();
            LocalDateTime startDate;
            
            // Calculate start date based on period
            switch (period) {
                case "7days":
                    startDate = now.minusDays(7);
                    break;
                case "30days":
                    startDate = now.minusDays(30);
                    break;
                case "90days":
                    startDate = now.minusDays(90);
                    break;
                case "1year":
                    startDate = now.minusYears(1);
                    break;
                default:
                    startDate = now.minusDays(30);
            }
            
            // Group refunds by date
            java.util.Map<String, BigDecimal> dailyRefunds = new java.util.HashMap<>();
            
            for (HoanTien refund : allRefunds) {
                if (refund.getNgayXuLy() != null && 
                    refund.getNgayXuLy().isAfter(startDate) && 
                    refund.getNgayXuLy().isBefore(now) &&
                    refund.getTrangThaiHoan().equals("Đã hoàn")) {
                    
                    String dateKey = refund.getNgayXuLy().toLocalDate().toString();
                    BigDecimal amount = dailyRefunds.getOrDefault(dateKey, BigDecimal.ZERO);
                    if (refund.getSoTienHoan() != null) {
                        dailyRefunds.put(dateKey, amount.add(refund.getSoTienHoan()));
                    }
                }
            }
            
            // Convert to sorted list for chart
            List<String> labels = new java.util.ArrayList<>(dailyRefunds.keySet());
            labels.sort(String::compareTo);
            
            List<Double> data = new java.util.ArrayList<>();
            for (String date : labels) {
                data.add(dailyRefunds.get(date).doubleValue());
            }
            
            java.util.Map<String, Object> result = new java.util.HashMap<>();
            result.put("labels", labels);
            result.put("data", data);
            
            return result;
        } catch (Exception e) {
            System.err.println("Error getting refund trend data: " + e.getMessage());
            e.printStackTrace();
            java.util.Map<String, Object> result = new java.util.HashMap<>();
            result.put("labels", new java.util.ArrayList<>());
            result.put("data", new java.util.ArrayList<>());
            return result;
        }
    }
    
    // Get refunds analysis by reason (LyDoHoan)
    public java.util.Map<String, Object> getRefundsByReason() {
        try {
            List<HoanTien> refunds = hoanTienRepository.findAll();
            
            // Group by LyDoHoan
            java.util.Map<String, java.util.List<HoanTien>> groupedByReason = new java.util.HashMap<>();
            java.util.Map<String, BigDecimal> totalByReason = new java.util.HashMap<>();
            
            for (HoanTien refund : refunds) {
                String reason = refund.getLyDoHoan() != null ? refund.getLyDoHoan() : "Không xác định";
                
                if (!groupedByReason.containsKey(reason)) {
                    groupedByReason.put(reason, new java.util.ArrayList<>());
                    totalByReason.put(reason, BigDecimal.ZERO);
                }
                
                groupedByReason.get(reason).add(refund);
                
                BigDecimal amount = refund.getSoTienHoan() != null ? refund.getSoTienHoan() : BigDecimal.ZERO;
                totalByReason.put(reason, totalByReason.get(reason).add(amount));
            }
            
            // Build result
            java.util.List<String> labels = new java.util.ArrayList<>(groupedByReason.keySet());
            java.util.List<Long> counts = new java.util.ArrayList<>();
            java.util.List<Double> amounts = new java.util.ArrayList<>();
            
            for (String reason : labels) {
                counts.add((long) groupedByReason.get(reason).size());
                amounts.add(totalByReason.get(reason).doubleValue());
            }
            
            java.util.Map<String, Object> result = new java.util.HashMap<>();
            result.put("labels", labels);
            result.put("counts", counts);
            result.put("amounts", amounts);
            
            return result;
        } catch (Exception e) {
            System.err.println("Error getting refunds by reason: " + e.getMessage());
            e.printStackTrace();
            java.util.Map<String, Object> result = new java.util.HashMap<>();
            result.put("labels", new java.util.ArrayList<>());
            result.put("counts", new java.util.ArrayList<>());
            result.put("amounts", new java.util.ArrayList<>());
            return result;
        }
    }
}
