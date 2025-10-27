package vn.ute.utescore.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "ThanhToan")
public class ThanhToan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer MaThanhToan;

    @ManyToOne
    @JoinColumn(name = "MaDonDat")
    private ThueSan thueSan;

    private BigDecimal SoTienNhan;

    @Column(columnDefinition = "NVARCHAR(50)", nullable = false)
    private String PhuongThuc;

    @Column(columnDefinition = "NVARCHAR(50)", nullable = false)
    private String LoaiThanhToan;

    @Column(columnDefinition = "NVARCHAR(50)", nullable = false)
    private String TrangThaiThanhToan;

    private Integer SoDiemSuDung;
    private BigDecimal GiaTriDiemGiam;
    private Integer SoDiemCongThem;

    private LocalDateTime NgayThanhToan;

    @Column(columnDefinition = "NVARCHAR(255)")
    private String MaGiaoDich;

    @Column(columnDefinition = "NVARCHAR(255)")
    private String GhiChu;

    // ===== Constructors =====
    public ThanhToan() {
    }

    public ThanhToan(Integer maThanhToan, ThueSan thueSan, BigDecimal soTienNhan, String phuongThuc,
                     String loaiThanhToan, String trangThaiThanhToan, Integer soDiemSuDung,
                     BigDecimal giaTriDiemGiam, Integer soDiemCongThem, LocalDateTime ngayThanhToan,
                     String maGiaoDich, String ghiChu) {
        this.MaThanhToan = maThanhToan;
        this.thueSan = thueSan;
        this.SoTienNhan = soTienNhan;
        this.PhuongThuc = phuongThuc;
        this.LoaiThanhToan = loaiThanhToan;
        this.TrangThaiThanhToan = trangThaiThanhToan;
        this.SoDiemSuDung = soDiemSuDung;
        this.GiaTriDiemGiam = giaTriDiemGiam;
        this.SoDiemCongThem = soDiemCongThem;
        this.NgayThanhToan = ngayThanhToan;
        this.MaGiaoDich = maGiaoDich;
        this.GhiChu = ghiChu;
    }

    // ===== Getters và Setters =====
    public Integer getMaThanhToan() {
        return MaThanhToan;
    }

    public void setMaThanhToan(Integer maThanhToan) {
        MaThanhToan = maThanhToan;
    }

    public ThueSan getThueSan() {
        return thueSan;
    }

    public void setThueSan(ThueSan thueSan) {
        this.thueSan = thueSan;
    }

    public BigDecimal getSoTienNhan() {
        return SoTienNhan;
    }

    public void setSoTienNhan(BigDecimal soTienNhan) {
        SoTienNhan = soTienNhan;
    }

    public String getPhuongThuc() {
        return PhuongThuc;
    }

    public void setPhuongThuc(String phuongThuc) {
        PhuongThuc = phuongThuc;
    }

    public String getLoaiThanhToan() {
        return LoaiThanhToan;
    }

    public void setLoaiThanhToan(String loaiThanhToan) {
        LoaiThanhToan = loaiThanhToan;
    }

    public String getTrangThaiThanhToan() {
        return TrangThaiThanhToan;
    }

    public void setTrangThaiThanhToan(String trangThaiThanhToan) {
        TrangThaiThanhToan = trangThaiThanhToan;
    }

    public Integer getSoDiemSuDung() {
        return SoDiemSuDung;
    }

    public void setSoDiemSuDung(Integer soDiemSuDung) {
        SoDiemSuDung = soDiemSuDung;
    }

    public BigDecimal getGiaTriDiemGiam() {
        return GiaTriDiemGiam;
    }

    public void setGiaTriDiemGiam(BigDecimal giaTriDiemGiam) {
        GiaTriDiemGiam = giaTriDiemGiam;
    }

    public Integer getSoDiemCongThem() {
        return SoDiemCongThem;
    }

    public void setSoDiemCongThem(Integer soDiemCongThem) {
        SoDiemCongThem = soDiemCongThem;
    }

    public LocalDateTime getNgayThanhToan() {
        return NgayThanhToan;
    }

    public void setNgayThanhToan(LocalDateTime ngayThanhToan) {
        NgayThanhToan = ngayThanhToan;
    }

    public String getMaGiaoDich() {
        return MaGiaoDich;
    }

    public void setMaGiaoDich(String maGiaoDich) {
        MaGiaoDich = maGiaoDich;
    }

    public String getGhiChu() {
        return GhiChu;
    }

    public void setGhiChu(String ghiChu) {
        GhiChu = ghiChu;
    }
}
