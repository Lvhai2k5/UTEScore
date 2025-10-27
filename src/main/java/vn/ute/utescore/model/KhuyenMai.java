package vn.ute.utescore.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "KhuyenMai")
public class KhuyenMai {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer KhuyenMaiID;

    @Column(columnDefinition = "NVARCHAR(255)")
    private String TieuDe;

    @Column(columnDefinition = "NVARCHAR(255)")
    private String NoiDung;

    @Lob
    private byte[] HinhAnh;

    private LocalDateTime NgayBatDau;
    private LocalDateTime NgayKetThuc;

    @Column(columnDefinition = "NVARCHAR(50)")
    private String TrangThai;

    // ===== Constructors =====
    public KhuyenMai() {
    }

    public KhuyenMai(Integer khuyenMaiID, String tieuDe, String noiDung,
                     byte[] hinhAnh, LocalDateTime ngayBatDau,
                     LocalDateTime ngayKetThuc, String trangThai) {
        this.KhuyenMaiID = khuyenMaiID;
        this.TieuDe = tieuDe;
        this.NoiDung = noiDung;
        this.HinhAnh = hinhAnh;
        this.NgayBatDau = ngayBatDau;
        this.NgayKetThuc = ngayKetThuc;
        this.TrangThai = trangThai;
    }

    // ===== Getters & Setters =====
    public Integer getKhuyenMaiID() {
        return KhuyenMaiID;
    }

    public void setKhuyenMaiID(Integer khuyenMaiID) {
        this.KhuyenMaiID = khuyenMaiID;
    }

    public String getTieuDe() {
        return TieuDe;
    }

    public void setTieuDe(String tieuDe) {
        this.TieuDe = tieuDe;
    }

    public String getNoiDung() {
        return NoiDung;
    }

    public void setNoiDung(String noiDung) {
        this.NoiDung = noiDung;
    }

    public byte[] getHinhAnh() {
        return HinhAnh;
    }

    public void setHinhAnh(byte[] hinhAnh) {
        this.HinhAnh = hinhAnh;
    }

    public LocalDateTime getNgayBatDau() {
        return NgayBatDau;
    }

    public void setNgayBatDau(LocalDateTime ngayBatDau) {
        this.NgayBatDau = ngayBatDau;
    }

    public LocalDateTime getNgayKetThuc() {
        return NgayKetThuc;
    }

    public void setNgayKetThuc(LocalDateTime ngayKetThuc) {
        this.NgayKetThuc = ngayKetThuc;
    }

    public String getTrangThai() {
        return TrangThai;
    }

    public void setTrangThai(String trangThai) {
        this.TrangThai = trangThai;
    }
}
