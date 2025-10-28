package vn.ute.utescore.model;

import jakarta.persistence.*;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "Roles")
public class Roles {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "RoleID")
    private Integer roleID;

    @Column(name = "RoleName", columnDefinition = "NVARCHAR(50)")
    private String roleName;

    @Column(name = "MoTa", columnDefinition = "NVARCHAR(255)")
    private String moTa;

    @OneToMany(mappedBy = "role", cascade = CascadeType.ALL)
    private List<NhanVien> nhanViens;

    @OneToMany(mappedBy = "role", cascade = CascadeType.ALL)
    private List<TaiKhoan> taiKhoans;

    @OneToMany(mappedBy = "role", cascade = CascadeType.ALL)
    private List<RolePermission> rolePermissions;

    public Roles() {}

    public Roles(Integer roleID, String roleName, String moTa) {
        this.roleID = roleID;
        this.roleName = roleName;
        this.moTa = moTa;
    }

    public Integer getRoleID() {
        return roleID;
    }

    public void setRoleID(Integer roleID) {
        this.roleID = roleID;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getMoTa() {
        return moTa;
    }

    public void setMoTa(String moTa) {
        this.moTa = moTa;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Roles)) return false;
        Roles roles = (Roles) o;
        return Objects.equals(roleID, roles.roleID);
    }

    @Override
    public int hashCode() {
        return Objects.hash(roleID);
    }
}
