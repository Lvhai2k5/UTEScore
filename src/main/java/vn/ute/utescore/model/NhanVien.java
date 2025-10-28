package vn.ute.utescore.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "NhanVien")
public class NhanVien {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "UserID")
    private Integer userID;

    @Column(name = "FullName", columnDefinition = "NVARCHAR(50)")
    private String fullName;

    @Column(name = "Email", columnDefinition = "NVARCHAR(100)")
    private String email;

    @Column(name = "Phone", columnDefinition = "NVARCHAR(11)")
    private String phone;

    @ManyToOne
    @JoinColumn(name = "RoleID")
    private Roles role;

    @Column(name = "Status", columnDefinition = "NVARCHAR(20)")
    private String status;

    @Column(name = "CreatedAt")
    private LocalDateTime createdAt;

    @Column(name = "UpdateAt")
    private LocalDateTime updateAt;

    @OneToMany(mappedBy = "nhanVien", cascade = CascadeType.ALL)
    private List<BaoTri> baoTris;

    @OneToMany(mappedBy = "nhanVien", cascade = CascadeType.ALL)
    private List<SuCo> suCos;

    @OneToMany(mappedBy = "nhanVienPhuTrach", cascade = CascadeType.ALL)
    private List<Camera> cameras;

    @OneToMany(mappedBy = "nhanVien", cascade = CascadeType.ALL)
    private List<LichSuCamera> lichSuCameras;

    @OneToMany(mappedBy = "nguoiThucHien", cascade = CascadeType.ALL)
    private List<LichSuTrangThaiSan> lichSuTrangThais;

    @OneToMany(mappedBy = "nhanVienXuLy", cascade = CascadeType.ALL)
    private List<GopYHeThong> gopYXuLys;

    @OneToMany(mappedBy = "nhanVienPhanHoi", cascade = CascadeType.ALL)
    private List<DanhGiaDonHang> danhGiaPhanHois;

    @OneToMany(mappedBy = "nhanVien", cascade = CascadeType.ALL)
    private List<CaLamViec> caLamViecs;

    public NhanVien() {}

    public Integer getUserID() {
        return userID;
    }
    public void setUserID(Integer userID) {
        this.userID = userID;
    }

    public String getFullName() {
        return fullName;
    }
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Roles getRole() {
        return role;
    }
    public void setRole(Roles role) {
        this.role = role;
    }

    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdateAt() {
        return updateAt;
    }
    public void setUpdateAt(LocalDateTime updateAt) {
        this.updateAt = updateAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof NhanVien)) return false;
        NhanVien nhanVien = (NhanVien) o;
        return Objects.equals(userID, nhanVien.userID);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userID);
    }
}
