package vn.ute.utescore.dto;

import java.time.LocalDateTime;
import java.time.LocalTime;

public class MaintenanceDTO {
    private Integer baoTriID;
    private String tenSan;
    private String loaiSan;
    private LocalTime thoiGianBatDau;
    private LocalTime thoiGianKetThuc;
    private LocalDateTime ngayBaoTri;
    private String fullName;
    private String trangThai;
    private String lyDo;

    public MaintenanceDTO() {
    }

    public MaintenanceDTO(Integer baoTriID, String tenSan, String loaiSan, 
                          LocalTime thoiGianBatDau, LocalTime thoiGianKetThuc,
                          LocalDateTime ngayBaoTri, String fullName, String trangThai, String lyDo) {
        this.baoTriID = baoTriID;
        this.tenSan = tenSan;
        this.loaiSan = loaiSan;
        this.thoiGianBatDau = thoiGianBatDau;
        this.thoiGianKetThuc = thoiGianKetThuc;
        this.ngayBaoTri = ngayBaoTri;
        this.fullName = fullName;
        this.trangThai = trangThai;
        this.lyDo = lyDo;
    }

    // Getters and Setters
    public Integer getBaoTriID() { return baoTriID; }
    public void setBaoTriID(Integer baoTriID) { this.baoTriID = baoTriID; }

    public String getTenSan() { return tenSan; }
    public void setTenSan(String tenSan) { this.tenSan = tenSan; }

    public String getLoaiSan() { return loaiSan; }
    public void setLoaiSan(String loaiSan) { this.loaiSan = loaiSan; }

    public LocalTime getThoiGianBatDau() { return thoiGianBatDau; }
    public void setThoiGianBatDau(LocalTime thoiGianBatDau) { this.thoiGianBatDau = thoiGianBatDau; }

    public LocalTime getThoiGianKetThuc() { return thoiGianKetThuc; }
    public void setThoiGianKetThuc(LocalTime thoiGianKetThuc) { this.thoiGianKetThuc = thoiGianKetThuc; }

    public LocalDateTime getNgayBaoTri() { return ngayBaoTri; }
    public void setNgayBaoTri(LocalDateTime ngayBaoTri) { this.ngayBaoTri = ngayBaoTri; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getTrangThai() { return trangThai; }
    public void setTrangThai(String trangThai) { this.trangThai = trangThai; }

    public String getLyDo() { return lyDo; }
    public void setLyDo(String lyDo) { this.lyDo = lyDo; }
}
