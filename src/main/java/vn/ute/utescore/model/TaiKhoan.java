package vn.ute.utescore.model;

import jakarta.persistence.*;

@Entity
@Table(name = "TaiKhoan")
public class TaiKhoan {

    @Id
    @Column(name = "Email", columnDefinition = "NVARCHAR(100)")
    private String email;

    @Column(name = "SoDienThoai", columnDefinition = "NVARCHAR(11)")
    private String soDienThoai;

    @Column(name = "MatKhau", columnDefinition = "NVARCHAR(100)")
    private String matKhau;

    @ManyToOne
    @JoinColumn(name = "RoleID")
    private Roles role;

    @Column(name = "TrangThai", columnDefinition = "NVARCHAR(20)")
    private String trangThai;

    // ===== Constructors =====
    public TaiKhoan() {
    }

    public TaiKhoan(String email, String soDienThoai, String matKhau, Roles role, String trangThai) {
        this.email = email;
        this.soDienThoai = soDienThoai;
        this.matKhau = matKhau;
        this.role = role;
        this.trangThai = trangThai;
    }

    // ===== Getters & Setters =====
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSoDienThoai() {
        return soDienThoai;
    }

    public void setSoDienThoai(String soDienThoai) {
        this.soDienThoai = soDienThoai;
    }

    public String getMatKhau() {
        return matKhau;
    }

    public void setMatKhau(String matKhau) {
        this.matKhau = matKhau;
    }

    public Roles getRole() {
        return role;
    }

    public void setRole(Roles role) {
        this.role = role;
    }

    public String getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(String trangThai) {
        this.trangThai = trangThai;
    }
}
