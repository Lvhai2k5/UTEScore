package vn.ute.utescore.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "TinTuc")
public class TinTuc {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "TinID")
    private Integer tinID;

    @Column(name = "TieuDe", columnDefinition = "NVARCHAR(255)")
    private String tieuDe;

    @Column(name = "NoiDung", columnDefinition = "NVARCHAR(255)")
    private String noiDung;

    @Column(name = "HinhAnh", columnDefinition = "NVARCHAR(500)")
    private String hinhAnh;

    @Column(name = "NgayDang")
    private LocalDateTime ngayDang;

    @Column(name = "TrangThai", columnDefinition = "NVARCHAR(50)")
    private String trangThai;

    public TinTuc() {}

    public TinTuc(Integer tinID, String tieuDe, String noiDung,
                  String hinhAnh, LocalDateTime ngayDang, String trangThai) {
        this.tinID = tinID;
        this.tieuDe = tieuDe;
        this.noiDung = noiDung;
        this.hinhAnh = hinhAnh;
        this.ngayDang = ngayDang;
        this.trangThai = trangThai;
    }

    public Integer getTinID() { return tinID; }
    public void setTinID(Integer tinID) { this.tinID = tinID; }

    public String getTieuDe() { return tieuDe; }
    public void setTieuDe(String tieuDe) { this.tieuDe = tieuDe; }

    public String getNoiDung() { return noiDung; }
    public void setNoiDung(String noiDung) { this.noiDung = noiDung; }

    public String getHinhAnh() { return hinhAnh; }
    public void setHinhAnh(String hinhAnh) { this.hinhAnh = hinhAnh; }

    public LocalDateTime getNgayDang() { return ngayDang; }
    public void setNgayDang(LocalDateTime ngayDang) { this.ngayDang = ngayDang; }

    public String getTrangThai() { return trangThai; }
    public void setTrangThai(String trangThai) { this.trangThai = trangThai; }
}
