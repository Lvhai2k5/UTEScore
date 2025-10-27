package vn.ute.utescore.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "LichSuCamera")
public class LichSuCamera {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer LichSuID;

    @ManyToOne
    @JoinColumn(name = "CameraID")
    private Camera camera;

    @ManyToOne
    @JoinColumn(name = "NhanVienID")
    private NhanVien nhanVien;

    @Column(columnDefinition = "NVARCHAR(30)")
    private String TrangThaiCu;

    @Column(columnDefinition = "NVARCHAR(30)")
    private String TrangThaiMoi;

    private LocalDateTime ThoiGianCapNhat;

    @Column(columnDefinition = "NVARCHAR(255)")
    private String GhiChu;

    // ===== Constructors =====
    public LichSuCamera() {
    }

    public LichSuCamera(Integer lichSuID, Camera camera, NhanVien nhanVien,
                        String trangThaiCu, String trangThaiMoi,
                        LocalDateTime thoiGianCapNhat, String ghiChu) {
        this.LichSuID = lichSuID;
        this.camera = camera;
        this.nhanVien = nhanVien;
        this.TrangThaiCu = trangThaiCu;
        this.TrangThaiMoi = trangThaiMoi;
        this.ThoiGianCapNhat = thoiGianCapNhat;
        this.GhiChu = ghiChu;
    }

    // ===== Getters & Setters =====
    public Integer getLichSuID() {
        return LichSuID;
    }

    public void setLichSuID(Integer lichSuID) {
        this.LichSuID = lichSuID;
    }

    public Camera getCamera() {
        return camera;
    }

    public void setCamera(Camera camera) {
        this.camera = camera;
    }

    public NhanVien getNhanVien() {
        return nhanVien;
    }

    public void setNhanVien(NhanVien nhanVien) {
        this.nhanVien = nhanVien;
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

    public LocalDateTime getThoiGianCapNhat() {
        return ThoiGianCapNhat;
    }

    public void setThoiGianCapNhat(LocalDateTime thoiGianCapNhat) {
        this.ThoiGianCapNhat = thoiGianCapNhat;
    }

    public String getGhiChu() {
        return GhiChu;
    }

    public void setGhiChu(String ghiChu) {
        this.GhiChu = ghiChu;
    }
}
