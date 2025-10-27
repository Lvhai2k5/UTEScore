package vn.ute.utescore.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "SuCo")
public class SuCo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer SuCoID;

    @ManyToOne
    @JoinColumn(name = "MaDon")
    private ThueSan thueSan;

    @Column(columnDefinition = "NVARCHAR(255)")
    private String MoTa;

    @Column(columnDefinition = "NVARCHAR(50)")
    private String LoaiSuCo;

    @ManyToOne
    @JoinColumn(name = "NhanVienID")
    private NhanVien nhanVien;

    private LocalDateTime ThoiGianBaoCao;

    @Column(columnDefinition = "NVARCHAR(50)")
    private String TrangThai;

    private LocalDateTime ThoiGianHoanThanh;

    // ===== Constructors =====
    public SuCo() {
    }

    public SuCo(Integer suCoID, ThueSan thueSan, String moTa, String loaiSuCo,
                NhanVien nhanVien, LocalDateTime thoiGianBaoCao,
                String trangThai, LocalDateTime thoiGianHoanThanh) {
        this.SuCoID = suCoID;
        this.thueSan = thueSan;
        this.MoTa = moTa;
        this.LoaiSuCo = loaiSuCo;
        this.nhanVien = nhanVien;
        this.ThoiGianBaoCao = thoiGianBaoCao;
        this.TrangThai = trangThai;
        this.ThoiGianHoanThanh = thoiGianHoanThanh;
    }

    // ===== Getters & Setters =====
    public Integer getSuCoID() {
        return SuCoID;
    }

    public void setSuCoID(Integer suCoID) {
        this.SuCoID = suCoID;
    }

    public ThueSan getThueSan() {
        return thueSan;
    }

    public void setThueSan(ThueSan thueSan) {
        this.thueSan = thueSan;
    }

    public String getMoTa() {
        return MoTa;
    }

    public void setMoTa(String moTa) {
        this.MoTa = moTa;
    }

    public String getLoaiSuCo() {
        return LoaiSuCo;
    }

    public void setLoaiSuCo(String loaiSuCo) {
        this.LoaiSuCo = loaiSuCo;
    }

    public NhanVien getNhanVien() {
        return nhanVien;
    }

    public void setNhanVien(NhanVien nhanVien) {
        this.nhanVien = nhanVien;
    }

    public LocalDateTime getThoiGianBaoCao() {
        return ThoiGianBaoCao;
    }

    public void setThoiGianBaoCao(LocalDateTime thoiGianBaoCao) {
        this.ThoiGianBaoCao = thoiGianBaoCao;
    }

    public String getTrangThai() {
        return TrangThai;
    }

    public void setTrangThai(String trangThai) {
        this.TrangThai = trangThai;
    }

    public LocalDateTime getThoiGianHoanThanh() {
        return ThoiGianHoanThanh;
    }

    public void setThoiGianHoanThanh(LocalDateTime thoiGianHoanThanh) {
        this.ThoiGianHoanThanh = thoiGianHoanThanh;
    }
}
