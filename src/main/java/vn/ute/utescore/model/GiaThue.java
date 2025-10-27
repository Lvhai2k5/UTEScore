package vn.ute.utescore.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Table(name = "GiaThue")
public class GiaThue {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer MaBangGia;

    @Column(columnDefinition = "NVARCHAR(20)")
    private String LoaiSan;

    private LocalTime KhungGioBatDau;
    private LocalTime KhungGioKetThuc;

    @Column(columnDefinition = "NVARCHAR(255)")
    private String MoTa;

    private BigDecimal GiaThue;
    private LocalDateTime NgayApDung;

    @Column(columnDefinition = "NVARCHAR(50)")
    private String TrangThai;

    // ===== Constructors =====
    public GiaThue() {
    }

    public GiaThue(Integer maBangGia, String loaiSan, LocalTime khungGioBatDau,
                   LocalTime khungGioKetThuc, String moTa, BigDecimal giaThue,
                   LocalDateTime ngayApDung, String trangThai) {
        this.MaBangGia = maBangGia;
        this.LoaiSan = loaiSan;
        this.KhungGioBatDau = khungGioBatDau;
        this.KhungGioKetThuc = khungGioKetThuc;
        this.MoTa = moTa;
        this.GiaThue = giaThue;
        this.NgayApDung = ngayApDung;
        this.TrangThai = trangThai;
    }

    // ===== Getters & Setters =====
    public Integer getMaBangGia() {
        return MaBangGia;
    }

    public void setMaBangGia(Integer maBangGia) {
        this.MaBangGia = maBangGia;
    }

    public String getLoaiSan() {
        return LoaiSan;
    }

    public void setLoaiSan(String loaiSan) {
        this.LoaiSan = loaiSan;
    }

    public LocalTime getKhungGioBatDau() {
        return KhungGioBatDau;
    }

    public void setKhungGioBatDau(LocalTime khungGioBatDau) {
        this.KhungGioBatDau = khungGioBatDau;
    }

    public LocalTime getKhungGioKetThuc() {
        return KhungGioKetThuc;
    }

    public void setKhungGioKetThuc(LocalTime khungGioKetThuc) {
        this.KhungGioKetThuc = khungGioKetThuc;
    }

    public String getMoTa() {
        return MoTa;
    }

    public void setMoTa(String moTa) {
        this.MoTa = moTa;
    }

    public BigDecimal getGiaThue() {
        return GiaThue;
    }

    public void setGiaThue(BigDecimal giaThue) {
        this.GiaThue = giaThue;
    }

    public LocalDateTime getNgayApDung() {
        return NgayApDung;
    }

    public void setNgayApDung(LocalDateTime ngayApDung) {
        this.NgayApDung = ngayApDung;
    }

    public String getTrangThai() {
        return TrangThai;
    }

    public void setTrangThai(String trangThai) {
        this.TrangThai = trangThai;
    }
}
