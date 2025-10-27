package vn.ute.utescore.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "KhachHang")
public class KhachHang {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MaKhachHang")
    private Integer maKhachHang;

    @Column(name = "HoTen", columnDefinition = "NVARCHAR(50)")
    private String hoTen;

    @Column(name = "SoDienThoai", columnDefinition = "NVARCHAR(11)")
    private String soDienThoai;

    @Column(name = "Email", columnDefinition = "NVARCHAR(100)")
    private String email;

    @Lob
    @Column(name = "AnhDaiDien")
    private byte[] anhDaiDien;

    @Column(name = "DiemTichLuy")
    private Integer diemTichLuy;

    @Column(name = "TrangThai", columnDefinition = "NVARCHAR(20)")
    private String trangThai;

    @Column(name = "NgayDangKy")
    private LocalDateTime ngayDangKy;

    @Column(name = "IsDeleted")
    private Boolean isDeleted;

    // ===== Quan hệ =====
    @OneToMany(mappedBy = "khachHang", cascade = CascadeType.ALL)
    private List<ThueSan> thueSans;

    @OneToMany(mappedBy = "khachHang", cascade = CascadeType.ALL)
    private List<ThongBao> thongBaos;

    @OneToMany(mappedBy = "khachHang", cascade = CascadeType.ALL)
    private List<GopYHeThong> gopYHeThongs;

    @OneToMany(mappedBy = "khachHang", cascade = CascadeType.ALL)
    private List<DanhGiaDonHang> danhGiaDonHangs;

    // ===== Constructors =====
    public KhachHang() {
    }

    public KhachHang(Integer maKhachHang, String hoTen, String soDienThoai, String email,
                     byte[] anhDaiDien, Integer diemTichLuy, String trangThai,
                     LocalDateTime ngayDangKy, Boolean isDeleted) {
        this.maKhachHang = maKhachHang;
        this.hoTen = hoTen;
        this.soDienThoai = soDienThoai;
        this.email = email;
        this.anhDaiDien = anhDaiDien;
        this.diemTichLuy = diemTichLuy;
        this.trangThai = trangThai;
        this.ngayDangKy = ngayDangKy;
        this.isDeleted = isDeleted;
    }

    // ===== Getter & Setter =====

    public Integer getMaKhachHang() {
        return maKhachHang;
    }

    public void setMaKhachHang(Integer maKhachHang) {
        this.maKhachHang = maKhachHang;
    }

    public String getHoTen() {
        return hoTen;
    }

    public void setHoTen(String hoTen) {
        this.hoTen = hoTen;
    }

    public String getSoDienThoai() {
        return soDienThoai;
    }

    public void setSoDienThoai(String soDienThoai) {
        this.soDienThoai = soDienThoai;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public byte[] getAnhDaiDien() {
        return anhDaiDien;
    }

    public void setAnhDaiDien(byte[] anhDaiDien) {
        this.anhDaiDien = anhDaiDien;
    }

    public Integer getDiemTichLuy() {
        return diemTichLuy;
    }

    public void setDiemTichLuy(Integer diemTichLuy) {
        this.diemTichLuy = diemTichLuy;
    }

    public String getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(String trangThai) {
        this.trangThai = trangThai;
    }

    public LocalDateTime getNgayDangKy() {
        return ngayDangKy;
    }

    public void setNgayDangKy(LocalDateTime ngayDangKy) {
        this.ngayDangKy = ngayDangKy;
    }

    public Boolean getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    public List<ThueSan> getThueSans() {
        return thueSans;
    }

    public void setThueSans(List<ThueSan> thueSans) {
        this.thueSans = thueSans;
    }

    public List<ThongBao> getThongBaos() {
        return thongBaos;
    }

    public void setThongBaos(List<ThongBao> thongBaos) {
        this.thongBaos = thongBaos;
    }

    public List<GopYHeThong> getGopYHeThongs() {
        return gopYHeThongs;
    }

    public void setGopYHeThongs(List<GopYHeThong> gopYHeThongs) {
        this.gopYHeThongs = gopYHeThongs;
    }

    public List<DanhGiaDonHang> getDanhGiaDonHangs() {
        return danhGiaDonHangs;
    }

    public void setDanhGiaDonHangs(List<DanhGiaDonHang> danhGiaDonHangs) {
        this.danhGiaDonHangs = danhGiaDonHangs;
    }
}
