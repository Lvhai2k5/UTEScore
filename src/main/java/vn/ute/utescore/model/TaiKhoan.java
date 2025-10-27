package vn.ute.utescore.model;

import jakarta.persistence.*;

@Entity
@Table(name = "TaiKhoan")
public class TaiKhoan {

    @Id
    @Column(columnDefinition = "NVARCHAR(100)")
    private String Email;

    @Column(columnDefinition = "NVARCHAR(11)")
    private String SoDienThoai;

    @Column(columnDefinition = "NVARCHAR(100)")
    private String MatKhau;

    @ManyToOne
    @JoinColumn(name = "RoleID")
    private Roles role;

    @Column(columnDefinition = "NVARCHAR(20)")
    private String TrangThai;

    // ===== Constructors =====
    public TaiKhoan() {
    }

    public TaiKhoan(String email, String soDienThoai, String matKhau, Roles role, String trangThai) {
        this.Email = email;
        this.SoDienThoai = soDienThoai;
        this.MatKhau = matKhau;
        this.role = role;
        this.TrangThai = trangThai;
    }

    // ===== Getters & Setters =====
    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        this.Email = email;
    }

    public String getSoDienThoai() {
        return SoDienThoai;
    }

    public void setSoDienThoai(String soDienThoai) {
        this.SoDienThoai = soDienThoai;
    }

    public String getMatKhau() {
        return MatKhau;
    }

    public void setMatKhau(String matKhau) {
        this.MatKhau = matKhau;
    }

    public Roles getRole() {
        return role;
    }

    public void setRole(Roles role) {
        this.role = role;
    }

    public String getTrangThai() {
        return TrangThai;
    }

    public void setTrangThai(String trangThai) {
        this.TrangThai = trangThai;
    }
}
