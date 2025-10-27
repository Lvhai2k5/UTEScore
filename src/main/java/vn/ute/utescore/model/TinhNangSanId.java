package vn.ute.utescore.model;

import jakarta.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
public class TinhNangSanId implements Serializable {

    private static final long serialVersionUID = 1L;
	private Integer MaTinhNang;
    private Integer MaSan;

    // ===== Constructors =====
    public TinhNangSanId() {
    }

    public TinhNangSanId(Integer maTinhNang, Integer maSan) {
        this.MaTinhNang = maTinhNang;
        this.MaSan = maSan;
    }

    // ===== Getters & Setters =====
    public Integer getMaTinhNang() {
        return MaTinhNang;
    }

    public void setMaTinhNang(Integer maTinhNang) {
        this.MaTinhNang = maTinhNang;
    }

    public Integer getMaSan() {
        return MaSan;
    }

    public void setMaSan(Integer maSan) {
        this.MaSan = maSan;
    }
}
