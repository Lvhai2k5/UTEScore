package vn.ute.utescore.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "HoanTien")
public class HoanTien {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MaHoanTien")
    private Integer maHoanTien;

    @ManyToOne
    @JoinColumn(name = "MaThanhToan", nullable = false)
    private ThanhToan thanhToan;

    @Column(name = "SoTienHoan", precision = 18, scale = 2, nullable = false)
    private BigDecimal soTienHoan;

    @Column(name = "LyDoHoan", columnDefinition = "NVARCHAR(255)")
    private String lyDoHoan;

    @Column(name = "NgayYeuCau")
    private LocalDateTime ngayYeuCau;

    @Column(name = "NgayXuLy")
    private LocalDateTime ngayXuLy;

    @Column(name = "TrangThaiHoan", columnDefinition = "NVARCHAR(50)")
    private String trangThaiHoan;

    @Column(name = "PhuongThucHoan", columnDefinition = "NVARCHAR(100)")
    private String phuongThucHoan;

    @Column(name = "SoTaiKhoan", columnDefinition = "NVARCHAR(50)")
    private String soTaiKhoan;

    @Column(name = "NganHang", columnDefinition = "NVARCHAR(100)")
    private String nganHang;

    @Column(name = "NguoiXuLy", columnDefinition = "NVARCHAR(100)")
    private String nguoiXuLy;

    @Column(name = "GhiChu", columnDefinition = "NVARCHAR(255)")
    private String ghiChu;

    // ===== CONSTRUCTOR =====
    public HoanTien() {}

    public HoanTien(Integer maHoanTien, ThanhToan thanhToan, BigDecimal soTienHoan,
                    String lyDoHoan, LocalDateTime ngayYeuCau, LocalDateTime ngayXuLy,
                    String trangThaiHoan, String phuongThucHoan, String soTaiKhoan,
                    String nganHang, String nguoiXuLy, String ghiChu) {
        this.maHoanTien = maHoanTien;
        this.thanhToan = thanhToan;
        this.soTienHoan = soTienHoan;
        this.lyDoHoan = lyDoHoan;
        this.ngayYeuCau = ngayYeuCau;
        this.ngayXuLy = ngayXuLy;
        this.trangThaiHoan = trangThaiHoan;
        this.phuongThucHoan = phuongThucHoan;
        this.soTaiKhoan = soTaiKhoan;
        this.nganHang = nganHang;
        this.nguoiXuLy = nguoiXuLy;
        this.ghiChu = ghiChu;
    }

    // ===== GETTERS & SETTERS =====
    public Integer getMaHoanTien() { return maHoanTien; }
    public void setMaHoanTien(Integer maHoanTien) { this.maHoanTien = maHoanTien; }

    public ThanhToan getThanhToan() { return thanhToan; }
    public void setThanhToan(ThanhToan thanhToan) { this.thanhToan = thanhToan; }

    public BigDecimal getSoTienHoan() { return soTienHoan; }
    public void setSoTienHoan(BigDecimal soTienHoan) { this.soTienHoan = soTienHoan; }

    public String getLyDoHoan() { return lyDoHoan; }
    public void setLyDoHoan(String lyDoHoan) { this.lyDoHoan = lyDoHoan; }

    public LocalDateTime getNgayYeuCau() { return ngayYeuCau; }
    public void setNgayYeuCau(LocalDateTime ngayYeuCau) { this.ngayYeuCau = ngayYeuCau; }

    public LocalDateTime getNgayXuLy() { return ngayXuLy; }
    public void setNgayXuLy(LocalDateTime ngayXuLy) { this.ngayXuLy = ngayXuLy; }

    public String getTrangThaiHoan() { return trangThaiHoan; }
    public void setTrangThaiHoan(String trangThaiHoan) { this.trangThaiHoan = trangThaiHoan; }

    public String getPhuongThucHoan() { return phuongThucHoan; }
    public void setPhuongThucHoan(String phuongThucHoan) { this.phuongThucHoan = phuongThucHoan; }

    public String getSoTaiKhoan() { return soTaiKhoan; }
    public void setSoTaiKhoan(String soTaiKhoan) { this.soTaiKhoan = soTaiKhoan; }

    public String getNganHang() { return nganHang; }
    public void setNganHang(String nganHang) { this.nganHang = nganHang; }

    public String getNguoiXuLy() { return nguoiXuLy; }
    public void setNguoiXuLy(String nguoiXuLy) { this.nguoiXuLy = nguoiXuLy; }

    public String getGhiChu() { return ghiChu; }
    public void setGhiChu(String ghiChu) { this.ghiChu = ghiChu; }
}
