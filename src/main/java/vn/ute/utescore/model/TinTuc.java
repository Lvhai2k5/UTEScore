package vn.ute.utescore.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "TinTuc")
public class TinTuc {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer TinID;

    @Column(columnDefinition = "NVARCHAR(255)")
    private String TieuDe;

    @Column(columnDefinition = "NVARCHAR(255)")
    private String NoiDung;

    @Lob
    private byte[] HinhAnh;

    private LocalDateTime NgayDang;

    @Column(columnDefinition = "NVARCHAR(50)")
    private String TrangThai;

    // ===== Constructors =====
    public TinTuc() {
    }

    public TinTuc(Integer tinID, String tieuDe, String noiDung,
                  byte[] hinhAnh, LocalDateTime ngayDang, String trangThai) {
        this.TinID = tinID;
        this.TieuDe = tieuDe;
        this.NoiDung = noiDung;
        this.HinhAnh = hinhAnh;
        this.NgayDang = ngayDang;
        this.TrangThai = trangThai;
    }

    // ===== Getters & Setters =====
    public Integer getTinID() {
        return TinID;
    }

    public void setTinID(Integer tinID) {
        this.TinID = tinID;
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

    public byte[] getHinhAnh() {
        return HinhAnh;
    }

    public void setHinhAnh(byte[] hinhAnh) {
        this.HinhAnh = hinhAnh;
    }

    public LocalDateTime getNgayDang() {
        return NgayDang;
    }

    public void setNgayDang(LocalDateTime ngayDang) {
        this.NgayDang = ngayDang;
    }

    public String getTrangThai() {
        return TrangThai;
    }

    public void setTrangThai(String trangThai) {
        this.TrangThai = trangThai;
    }
}
