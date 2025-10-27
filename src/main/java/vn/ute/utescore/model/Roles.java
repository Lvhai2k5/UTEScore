package vn.ute.utescore.model;

import jakarta.persistence.*;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "Roles")
public class Roles {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer RoleID;

    @Column(columnDefinition = "NVARCHAR(50)")
    private String RoleName;

    @Column(columnDefinition = "NVARCHAR(255)")
    private String MoTa;

    @OneToMany(mappedBy = "role", cascade = CascadeType.ALL)
    private List<NhanVien> nhanViens;

    @OneToMany(mappedBy = "role", cascade = CascadeType.ALL)
    private List<TaiKhoan> taiKhoans;

    @OneToMany(mappedBy = "role", cascade = CascadeType.ALL)
    private List<RolePermission> rolePermissions;

    // ===== Constructors =====
    public Roles() {
    }

    public Roles(Integer roleID, String roleName, String moTa,
                 List<NhanVien> nhanViens, List<TaiKhoan> taiKhoans,
                 List<RolePermission> rolePermissions) {
        this.RoleID = roleID;
        this.RoleName = roleName;
        this.MoTa = moTa;
        this.nhanViens = nhanViens;
        this.taiKhoans = taiKhoans;
        this.rolePermissions = rolePermissions;
    }

    // ===== Getters & Setters =====
    public Integer getRoleID() {
        return RoleID;
    }

    public void setRoleID(Integer roleID) {
        this.RoleID = roleID;
    }

    public String getRoleName() {
        return RoleName;
    }

    public void setRoleName(String roleName) {
        this.RoleName = roleName;
    }

    public String getMoTa() {
        return MoTa;
    }

    public void setMoTa(String moTa) {
        this.MoTa = moTa;
    }

    public List<NhanVien> getNhanViens() {
        return nhanViens;
    }

    public void setNhanViens(List<NhanVien> nhanViens) {
        this.nhanViens = nhanViens;
    }

    public List<TaiKhoan> getTaiKhoans() {
        return taiKhoans;
    }

    public void setTaiKhoans(List<TaiKhoan> taiKhoans) {
        this.taiKhoans = taiKhoans;
    }

    public List<RolePermission> getRolePermissions() {
        return rolePermissions;
    }

    public void setRolePermissions(List<RolePermission> rolePermissions) {
        this.rolePermissions = rolePermissions;
    }

    // ===== equals & hashCode =====
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Roles)) return false;
        Roles roles = (Roles) o;
        return Objects.equals(RoleID, roles.RoleID);
    }

    @Override
    public int hashCode() {
        return Objects.hash(RoleID);
    }

    // ===== toString =====
    @Override
    public String toString() {
        return "Roles{" +
                "RoleID=" + RoleID +
                ", RoleName='" + RoleName + '\'' +
                ", MoTa='" + MoTa + '\'' +
                ", nhanViens=" + (nhanViens != null ? nhanViens.size() : 0) +
                ", taiKhoans=" + (taiKhoans != null ? taiKhoans.size() : 0) +
                ", rolePermissions=" + (rolePermissions != null ? rolePermissions.size() : 0) +
                '}';
    }
}
