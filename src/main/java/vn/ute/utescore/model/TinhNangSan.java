package vn.ute.utescore.model;

import jakarta.persistence.*;

@Entity
@Table(name = "TinhNangSan")
public class TinhNangSan {

    @EmbeddedId
    private TinhNangSanId id;

    @ManyToOne
    @MapsId("MaTinhNang")
    @JoinColumn(name = "MaTinhNang")
    private TinhNang tinhNang;

    @ManyToOne
    @MapsId("MaSan")
    @JoinColumn(name = "MaSan")
    private SanBong sanBong;

    // ===== Constructors =====
    public TinhNangSan() {
    }

    public TinhNangSan(TinhNangSanId id, TinhNang tinhNang, SanBong sanBong) {
        this.id = id;
        this.tinhNang = tinhNang;
        this.sanBong = sanBong;
    }

    // ===== Getters & Setters =====
    public TinhNangSanId getId() {
        return id;
    }

    public void setId(TinhNangSanId id) {
        this.id = id;
    }

    public TinhNang getTinhNang() {
        return tinhNang;
    }

    public void setTinhNang(TinhNang tinhNang) {
        this.tinhNang = tinhNang;
    }

    public SanBong getSanBong() {
        return sanBong;
    }

    public void setSanBong(SanBong sanBong) {
        this.sanBong = sanBong;
    }
}
