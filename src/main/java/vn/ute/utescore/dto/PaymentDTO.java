package vn.ute.utescore.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public class PaymentDTO {
    @JsonProperty("maThanhToan")
    private Integer maThanhToan;
    
    @JsonProperty("maDonDat")
    private Integer maDonDat;
    
    @JsonProperty("soTienNhan")
    private BigDecimal soTienNhan;
    
    @JsonProperty("phuongThuc")
    private String phuongThuc;
    
    @JsonProperty("loaiThanhToan")
    private String loaiThanhToan;
    
    @JsonProperty("trangThaiThanhToan")
    private String trangThaiThanhToan;
    
    @JsonProperty("soDiemSuDung")
    private Integer soDiemSuDung;
    
    @JsonProperty("giaTriDiemGiam")
    private BigDecimal giaTriDiemGiam;
    
    @JsonProperty("soDiemCongThem")
    private Integer soDiemCongThem;
    
    @JsonProperty("ngayThanhToan")
    private LocalDateTime ngayThanhToan;
    
    @JsonProperty("maGiaoDich")
    private String maGiaoDich;
    
    @JsonProperty("ghiChu")
    private String ghiChu;
    
    // Customer info from ThueSan -> KhachHang
    @JsonProperty("maKhachHang")
    private Integer maKhachHang;
    
    @JsonProperty("hoTen")
    private String hoTen;
    
    @JsonProperty("soDienThoai")
    private String soDienThoai;
    
    @JsonProperty("email")
    private String email;
    
    // Field info from ThueSan -> SanBong
    @JsonProperty("maSan")
    private Integer maSan;
    
    @JsonProperty("tenSan")
    private String tenSan;
    
    @JsonProperty("loaiSan")
    private String loaiSan;
    
    public PaymentDTO() {
    }
    
    public PaymentDTO(Integer maThanhToan, Integer maDonDat, BigDecimal soTienNhan, String phuongThuc,
                     String loaiThanhToan, String trangThaiThanhToan, Integer soDiemSuDung,
                     BigDecimal giaTriDiemGiam, Integer soDiemCongThem, LocalDateTime ngayThanhToan,
                     String maGiaoDich, String ghiChu, Integer maKhachHang, String hoTen, String soDienThoai,
                     String email, Integer maSan, String tenSan, String loaiSan) {
        this.maThanhToan = maThanhToan;
        this.maDonDat = maDonDat;
        this.soTienNhan = soTienNhan;
        this.phuongThuc = phuongThuc;
        this.loaiThanhToan = loaiThanhToan;
        this.trangThaiThanhToan = trangThaiThanhToan;
        this.soDiemSuDung = soDiemSuDung;
        this.giaTriDiemGiam = giaTriDiemGiam;
        this.soDiemCongThem = soDiemCongThem;
        this.ngayThanhToan = ngayThanhToan;
        this.maGiaoDich = maGiaoDich;
        this.ghiChu = ghiChu;
        this.maKhachHang = maKhachHang;
        this.hoTen = hoTen;
        this.soDienThoai = soDienThoai;
        this.email = email;
        this.maSan = maSan;
        this.tenSan = tenSan;
        this.loaiSan = loaiSan;
    }
    
    // Getters and Setters
    public Integer getMaThanhToan() {
        return maThanhToan;
    }
    
    public void setMaThanhToan(Integer maThanhToan) {
        this.maThanhToan = maThanhToan;
    }
    
    public Integer getMaDonDat() {
        return maDonDat;
    }
    
    public void setMaDonDat(Integer maDonDat) {
        this.maDonDat = maDonDat;
    }
    
    public BigDecimal getSoTienNhan() {
        return soTienNhan;
    }
    
    public void setSoTienNhan(BigDecimal soTienNhan) {
        this.soTienNhan = soTienNhan;
    }
    
    public String getPhuongThuc() {
        return phuongThuc;
    }
    
    public void setPhuongThuc(String phuongThuc) {
        this.phuongThuc = phuongThuc;
    }
    
    public String getLoaiThanhToan() {
        return loaiThanhToan;
    }
    
    public void setLoaiThanhToan(String loaiThanhToan) {
        this.loaiThanhToan = loaiThanhToan;
    }
    
    public String getTrangThaiThanhToan() {
        return trangThaiThanhToan;
    }
    
    public void setTrangThaiThanhToan(String trangThaiThanhToan) {
        this.trangThaiThanhToan = trangThaiThanhToan;
    }
    
    public Integer getSoDiemSuDung() {
        return soDiemSuDung;
    }
    
    public void setSoDiemSuDung(Integer soDiemSuDung) {
        this.soDiemSuDung = soDiemSuDung;
    }
    
    public BigDecimal getGiaTriDiemGiam() {
        return giaTriDiemGiam;
    }
    
    public void setGiaTriDiemGiam(BigDecimal giaTriDiemGiam) {
        this.giaTriDiemGiam = giaTriDiemGiam;
    }
    
    public Integer getSoDiemCongThem() {
        return soDiemCongThem;
    }
    
    public void setSoDiemCongThem(Integer soDiemCongThem) {
        this.soDiemCongThem = soDiemCongThem;
    }
    
    public LocalDateTime getNgayThanhToan() {
        return ngayThanhToan;
    }
    
    public void setNgayThanhToan(LocalDateTime ngayThanhToan) {
        this.ngayThanhToan = ngayThanhToan;
    }
    
    public String getMaGiaoDich() {
        return maGiaoDich;
    }
    
    public void setMaGiaoDich(String maGiaoDich) {
        this.maGiaoDich = maGiaoDich;
    }
    
    public String getGhiChu() {
        return ghiChu;
    }
    
    public void setGhiChu(String ghiChu) {
        this.ghiChu = ghiChu;
    }
    
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
    
    public Integer getMaSan() {
        return maSan;
    }
    
    public void setMaSan(Integer maSan) {
        this.maSan = maSan;
    }
    
    public String getTenSan() {
        return tenSan;
    }
    
    public void setTenSan(String tenSan) {
        this.tenSan = tenSan;
    }
    
    public String getLoaiSan() {
        return loaiSan;
    }
    
    public void setLoaiSan(String loaiSan) {
        this.loaiSan = loaiSan;
    }
}

