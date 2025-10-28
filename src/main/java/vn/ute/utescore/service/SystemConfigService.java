package vn.ute.utescore.service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import vn.ute.utescore.model.ConfigurationHistory;
import vn.ute.utescore.model.NhanVien;
import vn.ute.utescore.model.SystemConfigurations;
import vn.ute.utescore.model.ThongBao;
import vn.ute.utescore.repository.ConfigurationHistoryRepository;
import vn.ute.utescore.repository.NhanVienRepository;
import vn.ute.utescore.repository.SystemConfigurationsRepository;
import vn.ute.utescore.repository.ThongBaoRepository;

@Service
public class SystemConfigService {
    
    @Autowired
    private SystemConfigurationsRepository systemConfigurationsRepository;
    
    @Autowired
    private ConfigurationHistoryRepository configurationHistoryRepository;
    
    @Autowired
    private NhanVienRepository nhanVienRepository;
    
    @Autowired
    private ThongBaoRepository thongBaoRepository;
    
    /**
     * Lưu hoặc cập nhật cấu hình hệ thống
     */
    @Transactional
    public Map<String, Object> saveConfiguration(Map<String, Object> configData, String changedBy) {
        Map<String, Object> result = new HashMap<>();
        
        try {
            // Các config keys từ form
            String[] configKeys = {
                "deposit_rate",           // Tỷ lệ tiền cọc
                "transaction_fee",        // Phí giao dịch
                "service_fee",            // Phí dịch vụ
                "refund_time",            // Thời gian hoàn tiền
                "cancel_before_24h",      // Hủy trước 24h
                "cancel_before_12h",      // Hủy trước 12h
                "cancel_under_12h"         // Hủy dưới 12h
            };
            
            int savedCount = 0;
            int updatedCount = 0;
            int failedCount = 0;
            
            // Lưu từng config
            for (String key : configKeys) {
                if (configData.containsKey(key)) {
                    String newValue = String.valueOf(configData.get(key));
                    boolean success = updateConfig(key, newValue, changedBy);
                    
                    if (success) {
                        updatedCount++;
                    } else {
                        failedCount++;
                    }
                    savedCount++;
                }
            }
            
            result.put("success", true);
            result.put("savedCount", savedCount);
            result.put("updatedCount", updatedCount);
            result.put("failedCount", failedCount);
            result.put("message", "Đã lưu " + savedCount + " cấu hình thành công!");
            
            // Gửi thông báo đến tất cả nhân viên
            sendNotificationToAllStaff(configData, changedBy);
            
        } catch (Exception e) {
            System.err.println("Error saving configurations: " + e.getMessage());
            e.printStackTrace();
            result.put("success", false);
            result.put("message", "Lỗi khi lưu cấu hình: " + e.getMessage());
        }
        
        return result;
    }
    
    /**
     * Cập nhật một config cụ thể
     */
    private boolean updateConfig(String configKey, String newValue, String changedBy) {
        try {
            // Lấy config hiện tại
            Optional<SystemConfigurations> existingConfig = 
                systemConfigurationsRepository.findByConfigKey(configKey);
            
            String oldValue = "N/A";
            
            if (existingConfig.isPresent()) {
                // Cập nhật config hiện tại
                oldValue = existingConfig.get().getConfigValue();
                
                // Lưu lịch sử thay đổi
                ConfigurationHistory history = new ConfigurationHistory();
                history.setConfigKey(configKey);
                history.setOldValue(oldValue);
                history.setNewValue(newValue);
                history.setChangedBy(changedBy);
                history.setChangeReason("Cập nhật cấu hình hệ thống");
                history.setCreatedAt(LocalDateTime.now());
                configurationHistoryRepository.save(history);
                
                // Cập nhật config
                SystemConfigurations config = existingConfig.get();
                config.setConfigValue(newValue);
                config.setUpdatedAt(LocalDateTime.now());
                systemConfigurationsRepository.save(config);
                
            } else {
                // Tạo config mới
                SystemConfigurations newConfig = new SystemConfigurations();
                newConfig.setConfigKey(configKey);
                newConfig.setConfigValue(newValue);
                newConfig.setConfigType("system");
                newConfig.setIsActive(true);
                newConfig.setCreatedAt(LocalDateTime.now());
                newConfig.setUpdatedAt(LocalDateTime.now());
                systemConfigurationsRepository.save(newConfig);
                
                // Lưu lịch sử thay đổi
                ConfigurationHistory history = new ConfigurationHistory();
                history.setConfigKey(configKey);
                history.setOldValue("N/A");
                history.setNewValue(newValue);
                history.setChangedBy(changedBy);
                history.setChangeReason("Tạo cấu hình mới");
                history.setCreatedAt(LocalDateTime.now());
                configurationHistoryRepository.save(history);
            }
            
            return true;
            
        } catch (Exception e) {
            System.err.println("Error updating config " + configKey + ": " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Tạo thông báo vào bảng ThongBao cho nhân viên khi có thay đổi cấu hình
     */
    private void sendNotificationToAllStaff(Map<String, Object> configData, String changedBy) {
        try {
            System.out.println("===== Bắt đầu tạo thông báo cho nhân viên =====");
            
            // Lấy danh sách nhân viên đang hoạt động
            List<NhanVien> activeStaff = nhanVienRepository.findActiveEmployees();
            
            if (activeStaff == null || activeStaff.isEmpty()) {
                System.out.println("⚠️ Không có nhân viên nào để gửi thông báo");
                return;
            }
            
            System.out.println("📋 Tìm thấy " + activeStaff.size() + " nhân viên đang hoạt động");
            
            // Tạo nội dung thông báo
            String tieuDe = "Thông báo thay đổi cấu hình hệ thống";
            String noiDung = createConfigChangeNotificationContent(configData, changedBy);
            
            // Tạo thông báo cho từng nhân viên
            int successCount = 0;
            for (NhanVien staff : activeStaff) {
                try {
                    System.out.println("📝 Đang tạo thông báo cho nhân viên: " + staff.getFullName());
                    
                    ThongBao thongBao = new ThongBao();
                    thongBao.setTieuDe(tieuDe);
                    thongBao.setNoiDung(noiDung);
                    thongBao.setLoaiThongBao("Cấu hình hệ thống");
                    thongBao.setNgayGui(LocalDateTime.now());
                    thongBao.setTrangThai("Chưa đọc");
                    // Note: ThongBao hiện tại có liên kết với KhachHang, không có với NhanVien
                    // Tạm thời để khachHang = null vì không có relationship với NhanVien
                    thongBao.setKhachHang(null);
                    
                    ThongBao saved = thongBaoRepository.save(thongBao);
                    successCount++;
                    
                    System.out.println("✅ Đã tạo thông báo ID: " + saved.getMaThongBao() + " cho nhân viên: " + staff.getFullName());
                } catch (Exception e) {
                    System.err.println("❌ Lỗi tạo thông báo cho nhân viên " + staff.getFullName() + ": " + e.getMessage());
                    e.printStackTrace();
                }
            }
            
            System.out.println("📧 Đã tạo " + successCount + "/" + activeStaff.size() + " thông báo");
            System.out.println("===== Hoàn thành tạo thông báo =====");
            
        } catch (Exception e) {
            System.err.println("❌ Error creating notifications: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Tạo nội dung thông báo
     */
    private String createConfigChangeNotificationContent(Map<String, Object> configData, String changedBy) {
        StringBuilder content = new StringBuilder();
        content.append("Hệ thống đã có thay đổi về cấu hình. Chi tiết như sau:\n\n");
        
        // Thêm các thay đổi cụ thể
        if (configData.containsKey("deposit_rate")) {
            content.append("• Tỷ lệ tiền cọc: ").append(configData.get("deposit_rate")).append("%\n");
        }
        if (configData.containsKey("transaction_fee")) {
            content.append("• Phí giao dịch: ").append(configData.get("transaction_fee")).append(" VNĐ\n");
        }
        if (configData.containsKey("service_fee")) {
            content.append("• Phí dịch vụ: ").append(configData.get("service_fee")).append("%\n");
        }
        if (configData.containsKey("refund_time")) {
            content.append("• Thời gian hoàn tiền: ").append(configData.get("refund_time")).append(" ngày\n");
        }
        if (configData.containsKey("cancel_before_24h")) {
            content.append("• Hủy trước 24h: ").append(configData.get("cancel_before_24h")).append("% hoàn tiền\n");
        }
        if (configData.containsKey("cancel_before_12h")) {
            content.append("• Hủy trước 12h: ").append(configData.get("cancel_before_12h")).append("% hoàn tiền\n");
        }
        if (configData.containsKey("cancel_under_12h")) {
            content.append("• Hủy dưới 12h: ").append(configData.get("cancel_under_12h")).append("% hoàn tiền\n");
        }
        
        content.append("\nNgười thay đổi: ").append(changedBy).append("\n");
        content.append("Thời gian: ").append(LocalDateTime.now()).append("\n");
        content.append("\nVui lòng cập nhật thông tin mới nhất!");
        
        return content.toString();
    }
    
    /**
     * Lấy tất cả config hiện tại
     */
    public Map<String, String> getAllConfigurations() {
        Map<String, String> configs = new HashMap<>();
        
        try {
            List<SystemConfigurations> allConfigs = systemConfigurationsRepository.findAllActive();
            
            for (SystemConfigurations config : allConfigs) {
                configs.put(config.getConfigKey(), config.getConfigValue());
            }
        } catch (Exception e) {
            System.err.println("Error getting all configurations: " + e.getMessage());
        }
        
        return configs;
    }
    
    /**
     * Lấy lịch sử thay đổi config
     */
    public List<ConfigurationHistory> getConfigurationHistory(String configKey) {
        return configurationHistoryRepository.findByConfigKeyOrderByCreatedAtDesc(configKey);
    }
}

