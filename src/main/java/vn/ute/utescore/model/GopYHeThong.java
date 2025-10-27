package vn.ute.utescore.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "GopYHeThong")
public class GopYHeThong {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer MaGopY;

    @ManyToOne
    @JoinColumn(name = "MaKhachHang")
    private KhachHang khachHang;

    @Column(columnDefinition = "NVARCHAR(50)")
    private String LoaiGopY;

    @ManyToOne
    @JoinColumn(name = "MaSan")
    private SanBong sanBong;

    @Column(columnDefinition = "NVARCHAR(255)")
    private String NoiDung;

    private LocalDateTime NgayGopY;

    @Column(columnDefinition = "NVARCHAR(50)")
    private String TrangThaiXuLy;

    @Column(columnDefinition = "NVARCHAR(255)")
    private String PhanHoiTuHeThong;

    @ManyToOne
    @JoinColumn(name = "MaNhanVienXuLy")
    private NhanVien nhanVienXuLy;

    // ===== Constructors =====
    public GopYHeThong() {
    }

    public GopYHeThong(Integer maGopY, KhachHang khachHang, String loaiGopY, SanBong sanBong,
                       String noiDung, LocalDateTime ngayGopY, String trangThaiXuLy,
                       String phanHoiTuHeThong, NhanVien nhanVienXuLy) {
        this.MaGopY = maGopY;
        this.khachHang = khachHang;
        this.LoaiGopY = loaiGopY;
        this.sanBong = sanBong;
        this.NoiDung = noiDung;
        this.NgayGopY = ngayGopY;
        this.TrangThaiXuLy = trangThaiXuLy;
        this.PhanHoiTuHeThong = phanHoiTuHeThong;
        this.nhanVienXuLy = nhanVienXuLy;
    }

    // ===== Getters & Setters =====
    public Integer getMaGopY() {
        return MaGopY;
    }

    public void setMaGopY(Integer maGopY) {
        this.MaGopY = maGopY;
    }

    public KhachHang getKhachHang() {
        return khachHang;
    }

    public void setKhachHang(KhachHang khachHang) {
        this.khachHang = khachHang;
    }

    public String getLoaiGopY() {
        return LoaiGopY;
    }

    public void setLoaiGopY(String loaiGopY) {
        this.LoaiGopY = loaiGopY;
    }

    public SanBong getSanBong() {
        return sanBong;
    }

    public void setSanBong(SanBong sanBong) {
        this.sanBong = sanBong;
    }

    public String getNoiDung() {
        return NoiDung;
    }

    public void setNoiDung(String noiDung) {
        this.NoiDung = noiDung;
    }

    public LocalDateTime getNgayGopY() {
        return NgayGopY;
    }

    public void setNgayGopY(LocalDateTime ngayGopY) {
        this.NgayGopY = ngayGopY;
    }

    public String getTrangThaiXuLy() {
        return TrangThaiXuLy;
    }

    public void setTrangThaiXuLy(String trangThaiXuLy) {
        this.TrangThaiXuLy = trangThaiXuLy;
    }

    public String getPhanHoiTuHeThong() {
        return PhanHoiTuHeThong;
    }

    public void setPhanHoiTuHeThong(String phanHoiTuHeThong) {
        this.PhanHoiTuHeThong = phanHoiTuHeThong;
    }

    public NhanVien getNhanVienXuLy() {
        return nhanVienXuLy;
    }

    public void setNhanVienXuLy(NhanVien nhanVienXuLy) {
        this.nhanVienXuLy = nhanVienXuLy;
    }
}
