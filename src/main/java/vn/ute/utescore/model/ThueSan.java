package vn.ute.utescore.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Entity
@Table(name = "ThueSan")
public class ThueSan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MaDonDat")
    private Integer maDonDat;

    @ManyToOne
    @JoinColumn(name = "MaKhachHang")
    private KhachHang khachHang;

    @ManyToOne
    @JoinColumn(name = "MaSan")
    private SanBong sanBong;

    @Column(name = "NgayThue")
    private LocalDateTime ngayThue;

    @Column(name = "KhungGioBatDau")
    private LocalTime khungGioBatDau;

    @Column(name = "KhungGioKetThuc")
    private LocalTime khungGioKetThuc;

    @Column(name = "TongTien")
    private BigDecimal tongTien;

    @Column(name = "TienCocBatBuoc")
    private BigDecimal tienCocBatBuoc;

    @Column(name = "SoTienConLai", insertable = false, updatable = false)
    private BigDecimal soTienConLai;

    @Column(name = "NgayTao")
    private LocalDateTime ngayTao;

    @Column(name = "HanGiuCho")
    private LocalDateTime hanGiuCho;

    @Column(name = "GhiChu", columnDefinition = "NVARCHAR(255)")
    private String ghiChu;

    @OneToMany(mappedBy = "thueSan", cascade = CascadeType.ALL)
    private List<ThanhToan> thanhToans;

    @OneToMany(mappedBy = "thueSan", cascade = CascadeType.ALL)
    private List<DanhGiaDonHang> danhGiaDonHangs;

    @OneToMany(mappedBy = "thueSan", cascade = CascadeType.ALL)
    private List<SuCo> suCos;
    
    

    // ======== CONSTRUCTORS ========
    public ThueSan() {
    }

    public ThueSan(Integer maDonDat, KhachHang khachHang, SanBong sanBong, LocalDateTime ngayThue,
                   LocalTime khungGioBatDau, LocalTime khungGioKetThuc, BigDecimal tongTien,
                   BigDecimal tienCocBatBuoc, BigDecimal soTienConLai, LocalDateTime ngayTao,
                   LocalDateTime hanGiuCho, String ghiChu) {
        this.maDonDat = maDonDat;
        this.khachHang = khachHang;
        this.sanBong = sanBong;
        this.ngayThue = ngayThue;
        this.khungGioBatDau = khungGioBatDau;
        this.khungGioKetThuc = khungGioKetThuc;
        this.tongTien = tongTien;
        this.tienCocBatBuoc = tienCocBatBuoc;
        this.soTienConLai = soTienConLai;
        this.ngayTao = ngayTao;
        this.hanGiuCho = hanGiuCho;
        this.ghiChu = ghiChu;
    }

    // ======== GETTER & SETTER ========

    public Integer getMaDonDat() {
        return maDonDat;
    }

    public void setMaDonDat(Integer maDonDat) {
        this.maDonDat = maDonDat;
    }

    public KhachHang getKhachHang() {
        return khachHang;
    }

    public void setKhachHang(KhachHang khachHang) {
        this.khachHang = khachHang;
    }

    public SanBong getSanBong() {
        return sanBong;
    }

    public void setSanBong(SanBong sanBong) {
        this.sanBong = sanBong;
    }

    public LocalDateTime getNgayThue() {
        return ngayThue;
    }

    public void setNgayThue(LocalDateTime ngayThue) {
        this.ngayThue = ngayThue;
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

    public BigDecimal getTongTien() {
        return tongTien;
    }

    public void setTongTien(BigDecimal tongTien) {
        this.tongTien = tongTien;
    }

    public BigDecimal getTienCocBatBuoc() {
        return tienCocBatBuoc;
    }

    public void setTienCocBatBuoc(BigDecimal tienCocBatBuoc) {
        this.tienCocBatBuoc = tienCocBatBuoc;
    }

    public BigDecimal getSoTienConLai() {
        return soTienConLai;
    }

    public void setSoTienConLai(BigDecimal soTienConLai) {
        this.soTienConLai = soTienConLai;
    }

    public LocalDateTime getNgayTao() {
        return ngayTao;
    }

    public void setNgayTao(LocalDateTime ngayTao) {
        this.ngayTao = ngayTao;
    }

    public LocalDateTime getHanGiuCho() {
        return hanGiuCho;
    }

    public void setHanGiuCho(LocalDateTime hanGiuCho) {
        this.hanGiuCho = hanGiuCho;
    }

    public String getGhiChu() {
        return ghiChu;
    }

    public void setGhiChu(String ghiChu) {
        this.ghiChu = ghiChu;
    }

    public List<ThanhToan> getThanhToans() {
        return thanhToans;
    }

    public void setThanhToans(List<ThanhToan> thanhToans) {
        this.thanhToans = thanhToans;
    }

    public List<DanhGiaDonHang> getDanhGiaDonHangs() {
        return danhGiaDonHangs;
    }

    public void setDanhGiaDonHangs(List<DanhGiaDonHang> danhGiaDonHangs) {
        this.danhGiaDonHangs = danhGiaDonHangs;
    }

    public List<SuCo> getSuCos() {
        return suCos;
    }

    public void setSuCos(List<SuCo> suCos) {
        this.suCos = suCos;
    }
}
