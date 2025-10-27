package vn.ute.utescore.model;

import jakarta.persistence.*;
import java.time.LocalTime;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "CaLam")
public class CaLam {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer MaCaLam;

    private LocalTime GioBatDau;
    private LocalTime GioKetThuc;

    @OneToMany(mappedBy = "caLam", cascade = CascadeType.ALL)
    private List<CaLamViec> caLamViecs;

    // ===== Constructors =====
    public CaLam() {
    }

    public CaLam(Integer maCaLam, LocalTime gioBatDau, LocalTime gioKetThuc, List<CaLamViec> caLamViecs) {
        this.MaCaLam = maCaLam;
        this.GioBatDau = gioBatDau;
        this.GioKetThuc = gioKetThuc;
        this.caLamViecs = caLamViecs;
    }

    // ===== Getters & Setters =====
    public Integer getMaCaLam() {
        return MaCaLam;
    }

    public void setMaCaLam(Integer maCaLam) {
        this.MaCaLam = maCaLam;
    }

    public LocalTime getGioBatDau() {
        return GioBatDau;
    }

    public void setGioBatDau(LocalTime gioBatDau) {
        this.GioBatDau = gioBatDau;
    }

    public LocalTime getGioKetThuc() {
        return GioKetThuc;
    }

    public void setGioKetThuc(LocalTime gioKetThuc) {
        this.GioKetThuc = gioKetThuc;
    }

    public List<CaLamViec> getCaLamViecs() {
        return caLamViecs;
    }

    public void setCaLamViecs(List<CaLamViec> caLamViecs) {
        this.caLamViecs = caLamViecs;
    }

    // ===== equals & hashCode =====
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CaLam)) return false;
        CaLam caLam = (CaLam) o;
        return Objects.equals(MaCaLam, caLam.MaCaLam);
    }

    @Override
    public int hashCode() {
        return Objects.hash(MaCaLam);
    }

    // ===== toString =====
    @Override
    public String toString() {
        return "CaLam{" +
                "MaCaLam=" + MaCaLam +
                ", GioBatDau=" + GioBatDau +
                ", GioKetThuc=" + GioKetThuc +
                ", caLamViecs=" + (caLamViecs != null ? caLamViecs.size() : 0) +
                '}';
    }
}
