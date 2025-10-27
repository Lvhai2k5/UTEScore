package vn.ute.utescore.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "ThongBao")
public class ThongBao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer MaThongBao;

    @ManyToOne
    @JoinColumn(name = "MaKhachHang")
    private KhachHang khachHang;

    @Column(columnDefinition = "NVARCHAR(255)")
    private String TieuDe;

    @Column(columnDefinition = "NVARCHAR(255)")
    private String NoiDung;

    @Column(columnDefinition = "NVARCHAR(50)")
    private String LoaiThongBao;

    private LocalDateTime NgayGui;

    @Column(columnDefinition = "NVARCHAR(50)")
    private String TrangThai;

    // ===== Constructors =====
    public ThongBao() {
    }

    public ThongBao(Integer maThongBao, KhachHang khachHang, String tieuDe,
                    String noiDung, String loaiThongBao,
                    LocalDateTime ngayGui, String trangThai) {
        this.MaThongBao = maThongBao;
        this.khachHang = khachHang;
        this.TieuDe = tieuDe;
        this.NoiDung = noiDung;
        this.LoaiThongBao = loaiThongBao;
        this.NgayGui = ngayGui;
        this.TrangThai = trangThai;
    }

    // ===== Getters & Setters =====
    public Integer getMaThongBao() {
        return MaThongBao;
    }

    public void setMaThongBao(Integer maThongBao) {
        this.MaThongBao = maThongBao;
    }

    public KhachHang getKhachHang() {
        return khachHang;
    }

    public void setKhachHang(KhachHang khachHang) {
        this.khachHang = khachHang;
    }

    public String getTieuDe() {
        return TieuDe;
    }

    public void setTieuDe(String tieuDe) {
        this.TieuDe = tieuDe;
    }

    public String getNoiDung() {
        return NoiDung;
    }

    public void setNoiDung(String noiDung) {
        this.NoiDung = noiDung;
    }

    public String getLoaiThongBao() {
        return LoaiThongBao;
    }

    public void setLoaiThongBao(String loaiThongBao) {
        this.LoaiThongBao = loaiThongBao;
    }

    public LocalDateTime getNgayGui() {
        return NgayGui;
    }

    public void setNgayGui(LocalDateTime ngayGui) {
        this.NgayGui = ngayGui;
    }

    public String getTrangThai() {
        return TrangThai;
    }

    public void setTrangThai(String trangThai) {
        this.TrangThai = trangThai;
    }
}
