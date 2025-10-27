package vn.ute.utescore.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Objects;

@Entity
@Table(name = "BaoTri")
public class BaoTri {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer BaoTriID;

    @ManyToOne
    @JoinColumn(name = "MaSan")
    private SanBong sanBong;

    @ManyToOne
    @JoinColumn(name = "NhanVienID")
    private NhanVien nhanVien;

    private LocalTime ThoiGianBatDau;
    private LocalTime ThoiGianKetThuc;
    private LocalDateTime NgayBaoTri;

    @Column(columnDefinition = "NVARCHAR(255)")
    private String LyDo;

    @Column(columnDefinition = "NVARCHAR(50)")
    private String TrangThai;

    // ===== Constructors =====
    public BaoTri() {
    }

    public BaoTri(Integer baoTriID, SanBong sanBong, NhanVien nhanVien,
                  LocalTime thoiGianBatDau, LocalTime thoiGianKetThuc,
                  LocalDateTime ngayBaoTri, String lyDo, String trangThai) {
        this.BaoTriID = baoTriID;
        this.sanBong = sanBong;
        this.nhanVien = nhanVien;
        this.ThoiGianBatDau = thoiGianBatDau;
        this.ThoiGianKetThuc = thoiGianKetThuc;
        this.NgayBaoTri = ngayBaoTri;
        this.LyDo = lyDo;
        this.TrangThai = trangThai;
    }

    // ===== Getters & Setters =====
    public Integer getBaoTriID() {
        return BaoTriID;
    }

    public void setBaoTriID(Integer baoTriID) {
        this.BaoTriID = baoTriID;
    }

    public SanBong getSanBong() {
        return sanBong;
    }

    public void setSanBong(SanBong sanBong) {
        this.sanBong = sanBong;
    }

    public NhanVien getNhanVien() {
        return nhanVien;
    }

    public void setNhanVien(NhanVien nhanVien) {
        this.nhanVien = nhanVien;
    }

    public LocalTime getThoiGianBatDau() {
        return ThoiGianBatDau;
    }

    public void setThoiGianBatDau(LocalTime thoiGianBatDau) {
        this.ThoiGianBatDau = thoiGianBatDau;
    }

    public LocalTime getThoiGianKetThuc() {
        return ThoiGianKetThuc;
    }

    public void setThoiGianKetThuc(LocalTime thoiGianKetThuc) {
        this.ThoiGianKetThuc = thoiGianKetThuc;
    }

    public LocalDateTime getNgayBaoTri() {
        return NgayBaoTri;
    }

    public void setNgayBaoTri(LocalDateTime ngayBaoTri) {
        this.NgayBaoTri = ngayBaoTri;
    }

    public String getLyDo() {
        return LyDo;
    }

    public void setLyDo(String lyDo) {
        this.LyDo = lyDo;
    }

    public String getTrangThai() {
        return TrangThai;
    }

    public void setTrangThai(String trangThai) {
        this.TrangThai = trangThai;
    }

    // ===== equals & hashCode =====
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BaoTri)) return false;
        BaoTri baoTri = (BaoTri) o;
        return Objects.equals(BaoTriID, baoTri.BaoTriID);
    }

    @Override
    public int hashCode() {
        return Objects.hash(BaoTriID);
    }

//    // ===== toString =====
//    @Override
//    public String toString() {
//        return "BaoTri{" +
//                "BaoTriID=" + BaoTriID +
//                ", sanBong=" + (sanBong != null ? sanBong.getMaSan() : "null") +
//                ", nhanVien=" + (nhanVien != null ? nhanVien.getNhanVienID() : "null") +
//                ", ThoiGianBatDau=" + ThoiGianBatDau +
//                ", ThoiGianKetThuc=" + ThoiGianKetThuc +
//                ", NgayBaoTri=" + NgayBaoTri +
//                ", LyDo='" + LyDo + '\'' +
//                ", TrangThai='" + TrangThai + '\'' +
//                '}';
//    }
}
