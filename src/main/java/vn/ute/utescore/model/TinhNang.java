package vn.ute.utescore.model;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "TinhNang")
public class TinhNang {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer MaTinhNang;

    @Column(columnDefinition = "NVARCHAR(20)")
    private String TenTinhNang;

    @Column(columnDefinition = "NVARCHAR(255)")
    private String MoTa;

    @OneToMany(mappedBy = "tinhNang", cascade = CascadeType.ALL)
    private List<TinhNangSan> tinhNangSans;

    // ===== Constructors =====
    public TinhNang() {
    }

    public TinhNang(Integer maTinhNang, String tenTinhNang, String moTa, List<TinhNangSan> tinhNangSans) {
        this.MaTinhNang = maTinhNang;
        this.TenTinhNang = tenTinhNang;
        this.MoTa = moTa;
        this.tinhNangSans = tinhNangSans;
    }

    // ===== Getters & Setters =====
    public Integer getMaTinhNang() {
        return MaTinhNang;
    }

    public void setMaTinhNang(Integer maTinhNang) {
        this.MaTinhNang = maTinhNang;
    }

    public String getTenTinhNang() {
        return TenTinhNang;
    }

    public void setTenTinhNang(String tenTinhNang) {
        this.TenTinhNang = tenTinhNang;
    }

    public String getMoTa() {
        return MoTa;
    }

    public void setMoTa(String moTa) {
        this.MoTa = moTa;
    }

    public List<TinhNangSan> getTinhNangSans() {
        return tinhNangSans;
    }

    public void setTinhNangSans(List<TinhNangSan> tinhNangSans) {
        this.tinhNangSans = tinhNangSans;
    }
}
