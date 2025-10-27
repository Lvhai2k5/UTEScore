package vn.ute.utescore.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "LichSuTrangThaiSan")
public class LichSuTrangThaiSan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer LichSuID;

    @ManyToOne
    @JoinColumn(name = "SanID")
    private SanBong sanBong;

    @Column(columnDefinition = "NVARCHAR(50)")
    private String TrangThaiCu;

    @Column(columnDefinition = "NVARCHAR(50)")
    private String TrangThaiMoi;

    private LocalDateTime ThoiGianThayDoi;

    @ManyToOne
    @JoinColumn(name = "NguoiThucHien")
    private NhanVien nguoiThucHien;

    @Column(columnDefinition = "NVARCHAR(255)")
    private String GhiChu;

    // ===== Constructors =====
    public LichSuTrangThaiSan() {
    }

    public LichSuTrangThaiSan(Integer lichSuID, SanBong sanBong, String trangThaiCu,
                              String trangThaiMoi, LocalDateTime thoiGianThayDoi,
                              NhanVien nguoiThucHien, String ghiChu) {
        this.LichSuID = lichSuID;
        this.sanBong = sanBong;
        this.TrangThaiCu = trangThaiCu;
        this.TrangThaiMoi = trangThaiMoi;
        this.ThoiGianThayDoi = thoiGianThayDoi;
        this.nguoiThucHien = nguoiThucHien;
        this.GhiChu = ghiChu;
    }

    // ===== Getters & Setters =====
    public Integer getLichSuID() {
        return LichSuID;
    }

    public void setLichSuID(Integer lichSuID) {
        this.LichSuID = lichSuID;
    }

    public SanBong getSanBong() {
        return sanBong;
    }

    public void setSanBong(SanBong sanBong) {
        this.sanBong = sanBong;
    }

    public String getTrangThaiCu() {
        return TrangThaiCu;
    }

    public void setTrangThaiCu(String trangThaiCu) {
        this.TrangThaiCu = trangThaiCu;
    }

    public String getTrangThaiMoi() {
        return TrangThaiMoi;
    }

    public void setTrangThaiMoi(String trangThaiMoi) {
        this.TrangThaiMoi = trangThaiMoi;
    }

    public LocalDateTime getThoiGianThayDoi() {
        return ThoiGianThayDoi;
    }

    public void setThoiGianThayDoi(LocalDateTime thoiGianThayDoi) {
        this.ThoiGianThayDoi = thoiGianThayDoi;
    }

    public NhanVien getNguoiThucHien() {
        return nguoiThucHien;
    }

    public void setNguoiThucHien(NhanVien nguoiThucHien) {
        this.nguoiThucHien = nguoiThucHien;
    }

    public String getGhiChu() {
        return GhiChu;
    }

    public void setGhiChu(String ghiChu) {
        this.GhiChu = ghiChu;
    }
}
