package vn.ute.utescore.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "CaLamViec")
public class CaLamViec {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer CaID;

    @ManyToOne
    @JoinColumn(name = "NhanVienID")
    private NhanVien nhanVien;

    private LocalDateTime NgayLam;

    @ManyToOne
    @JoinColumn(name = "MaCaLam")
    private CaLam caLam;

    private Integer TongDonXuLy;
    private BigDecimal TongDoanhThu;

    // ===== Constructors =====
    public CaLamViec() {
    }

    public CaLamViec(Integer caID, NhanVien nhanVien, LocalDateTime ngayLam,
                     CaLam caLam, Integer tongDonXuLy, BigDecimal tongDoanhThu) {
        this.CaID = caID;
        this.nhanVien = nhanVien;
        this.NgayLam = ngayLam;
        this.caLam = caLam;
        this.TongDonXuLy = tongDonXuLy;
        this.TongDoanhThu = tongDoanhThu;
    }

    // ===== Getters & Setters =====
    public Integer getCaID() {
        return CaID;
    }

    public void setCaID(Integer caID) {
        this.CaID = caID;
    }

    public NhanVien getNhanVien() {
        return nhanVien;
    }

    public void setNhanVien(NhanVien nhanVien) {
        this.nhanVien = nhanVien;
    }

    public LocalDateTime getNgayLam() {
        return NgayLam;
    }

    public void setNgayLam(LocalDateTime ngayLam) {
        this.NgayLam = ngayLam;
    }

    public CaLam getCaLam() {
        return caLam;
    }

    public void setCaLam(CaLam caLam) {
        this.caLam = caLam;
    }

    public Integer getTongDonXuLy() {
        return TongDonXuLy;
    }

    public void setTongDonXuLy(Integer tongDonXuLy) {
        this.TongDonXuLy = tongDonXuLy;
    }

    public BigDecimal getTongDoanhThu() {
        return TongDoanhThu;
    }

    public void setTongDoanhThu(BigDecimal tongDoanhThu) {
        this.TongDoanhThu = tongDoanhThu;
    }

    // ===== equals & hashCode =====
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CaLamViec)) return false;
        CaLamViec that = (CaLamViec) o;
        return Objects.equals(CaID, that.CaID);
    }

    @Override
    public int hashCode() {
        return Objects.hash(CaID);
    }

    // ===== toString =====
    @Override
    public String toString() {
        return "CaLamViec{" +
                "CaID=" + CaID +
                ", nhanVien=" + (nhanVien != null ? nhanVien.getUserID() : "null") +
                ", NgayLam=" + NgayLam +
                ", caLam=" + (caLam != null ? caLam.getMaCaLam() : "null") +
                ", TongDonXuLy=" + TongDonXuLy +
                ", TongDoanhThu=" + TongDoanhThu +
                '}';
    }
}
