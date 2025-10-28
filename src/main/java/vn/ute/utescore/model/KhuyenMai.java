package vn.ute.utescore.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "KhuyenMai")
public class KhuyenMai {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "KhuyenMaiID")
    private Integer khuyenMaiID;

    @Column(name = "TieuDe", columnDefinition = "NVARCHAR(255)")
    private String tieuDe;

    @Column(name = "NoiDung", columnDefinition = "NVARCHAR(255)")
    private String noiDung;

    @Column(name = "HinhAnh", columnDefinition = "NVARCHAR(500)")
    private String hinhAnh;

    @Column(name = "NgayBatDau")
    private LocalDateTime ngayBatDau;

    @Column(name = "NgayKetThuc")
    private LocalDateTime ngayKetThuc;

    @Column(name = "TrangThai", columnDefinition = "NVARCHAR(50)")
    private String trangThai;

    public KhuyenMai() {}

    public KhuyenMai(Integer khuyenMaiID, String tieuDe, String noiDung,
                     String hinhAnh, LocalDateTime ngayBatDau,
                     LocalDateTime ngayKetThuc, String trangThai) {
        this.khuyenMaiID = khuyenMaiID;
        this.tieuDe = tieuDe;
        this.noiDung = noiDung;
        this.hinhAnh = hinhAnh;
        this.ngayBatDau = ngayBatDau;
        this.ngayKetThuc = ngayKetThuc;
        this.trangThai = trangThai;
    }

    public Integer getKhuyenMaiID() { return khuyenMaiID; }
    public void setKhuyenMaiID(Integer khuyenMaiID) { this.khuyenMaiID = khuyenMaiID; }

    public String getTieuDe() { return tieuDe; }
    public void setTieuDe(String tieuDe) { this.tieuDe = tieuDe; }

    public String getNoiDung() { return noiDung; }
    public void setNoiDung(String noiDung) { this.noiDung = noiDung; }

    public String getHinhAnh() { return hinhAnh; }
    public void setHinhAnh(String hinhAnh) { this.hinhAnh = hinhAnh; }

    public LocalDateTime getNgayBatDau() { return ngayBatDau; }
    public void setNgayBatDau(LocalDateTime ngayBatDau) { this.ngayBatDau = ngayBatDau; }

    public LocalDateTime getNgayKetThuc() { return ngayKetThuc; }
    public void setNgayKetThuc(LocalDateTime ngayKetThuc) { this.ngayKetThuc = ngayKetThuc; }

    public String getTrangThai() { return trangThai; }
    public void setTrangThai(String trangThai) { this.trangThai = trangThai; }
}
