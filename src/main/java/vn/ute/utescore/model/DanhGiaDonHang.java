package vn.ute.utescore.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "DanhGiaDonHang")
public class DanhGiaDonHang {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer MaDanhGia;

    @ManyToOne
    @JoinColumn(name = "MaDonDat")
    private ThueSan thueSan;

    @ManyToOne
    @JoinColumn(name = "MaKhachHang")
    private KhachHang khachHang;

    private Integer Rating;

    @Column(columnDefinition = "NVARCHAR(255)")
    private String NoiDung;

    private LocalDateTime NgayDanhGia;

    @Column(columnDefinition = "NVARCHAR(255)")
    private String TrangThaiPhanHoi;

    @ManyToOne
    @JoinColumn(name = "MaNhanVienPhanHoi")
    private NhanVien nhanVienPhanHoi;

    // ===== Constructors =====
    public DanhGiaDonHang() {
    }

    public DanhGiaDonHang(Integer maDanhGia, ThueSan thueSan, KhachHang khachHang,
                          Integer rating, String noiDung, LocalDateTime ngayDanhGia,
                          String trangThaiPhanHoi, NhanVien nhanVienPhanHoi) {
        this.MaDanhGia = maDanhGia;
        this.thueSan = thueSan;
        this.khachHang = khachHang;
        this.Rating = rating;
        this.NoiDung = noiDung;
        this.NgayDanhGia = ngayDanhGia;
        this.TrangThaiPhanHoi = trangThaiPhanHoi;
        this.nhanVienPhanHoi = nhanVienPhanHoi;
    }

    // ===== Getters & Setters =====
    public Integer getMaDanhGia() {
        return MaDanhGia;
    }

    public void setMaDanhGia(Integer maDanhGia) {
        this.MaDanhGia = maDanhGia;
    }

    public ThueSan getThueSan() {
        return thueSan;
    }

    public void setThueSan(ThueSan thueSan) {
        this.thueSan = thueSan;
    }

    public KhachHang getKhachHang() {
        return khachHang;
    }

    public void setKhachHang(KhachHang khachHang) {
        this.khachHang = khachHang;
    }

    public Integer getRating() {
        return Rating;
    }

    public void setRating(Integer rating) {
        this.Rating = rating;
    }

    public String getNoiDung() {
        return NoiDung;
    }

    public void setNoiDung(String noiDung) {
        this.NoiDung = noiDung;
    }

    public LocalDateTime getNgayDanhGia() {
        return NgayDanhGia;
    }

    public void setNgayDanhGia(LocalDateTime ngayDanhGia) {
        this.NgayDanhGia = ngayDanhGia;
    }

    public String getTrangThaiPhanHoi() {
        return TrangThaiPhanHoi;
    }

    public void setTrangThaiPhanHoi(String trangThaiPhanHoi) {
        this.TrangThaiPhanHoi = trangThaiPhanHoi;
    }

    public NhanVien getNhanVienPhanHoi() {
        return nhanVienPhanHoi;
    }

    public void setNhanVienPhanHoi(NhanVien nhanVienPhanHoi) {
        this.nhanVienPhanHoi = nhanVienPhanHoi;
    }
}
