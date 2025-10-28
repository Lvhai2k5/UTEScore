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
	    private Integer maBangGia;

	    @Column(name = "LoaiSan", columnDefinition = "NVARCHAR(20)")
	    private String loaiSan;

	    private LocalTime khungGioBatDau;
	    private LocalTime khungGioKetThuc;

	    @Column(columnDefinition = "NVARCHAR(255)")
	    private String moTa;

	    private BigDecimal giaThue;
	    private LocalDateTime ngayApDung;

	    @Column(columnDefinition = "NVARCHAR(50)")
	    private String trangThai;

    // ===== Constructors =====
    public GiaThue() {
    }

    public GiaThue(Integer maBangGia, String loaiSan, LocalTime khungGioBatDau,
                   LocalTime khungGioKetThuc, String moTa, BigDecimal giaThue,
                   LocalDateTime ngayApDung, String trangThai) {
        this.maBangGia = maBangGia;
        this.loaiSan = loaiSan;
        this.khungGioBatDau = khungGioBatDau;
        this.khungGioKetThuc = khungGioKetThuc;
        this.moTa = moTa;
        this.giaThue = giaThue;
        this.ngayApDung = ngayApDung;
        this.trangThai = trangThai;
    }

    // ===== Getters & Setters =====
    public Integer getMaBangGia() {
        return maBangGia;
    }

    public void setMaBangGia(Integer maBangGia) {
        this.maBangGia = maBangGia;
    }

    public String getLoaiSan() {
        return loaiSan;
    }

    public void setLoaiSan(String loaiSan) {
        this.loaiSan = loaiSan;
    }

    public LocalTime getKhungGioBatDau() {
        return khungGioBatDau;
    }

    public void setKhungGioBatDau(LocalTime khungGioBatDau) {
        this.khungGioBatDau = khungGioBatDau;
    }

    public LocalTime getKhungGioKetThuc() {
        return khungGioKetThuc;
    }

    public void setKhungGioKetThuc(LocalTime khungGioKetThuc) {
        this.khungGioKetThuc = khungGioKetThuc;
    }

    public String getMoTa() {
        return moTa;
    }

    public void setMoTa(String moTa) {
        this.moTa = moTa;
    }

    public BigDecimal getGiaThue() {
        return giaThue;
    }

    public void setGiaThue(BigDecimal giaThue) {
        this.giaThue = giaThue;
    }

    public LocalDateTime getNgayApDung() {
        return ngayApDung;
    }

    public void setNgayApDung(LocalDateTime ngayApDung) {
        this.ngayApDung = ngayApDung;
    }

    public String getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(String trangThai) {
        this.trangThai = trangThai;
    }
}
