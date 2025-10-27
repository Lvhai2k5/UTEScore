package vn.ute.utescore.model;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "Permission")
public class Permission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer PermissionID;

    @Column(columnDefinition = "NVARCHAR(255)")
    private String PermissionName;

    @Column(columnDefinition = "NVARCHAR(255)")
    private String MoTa;

    @OneToMany(mappedBy = "permission", cascade = CascadeType.ALL)
    private List<RolePermission> rolePermissions;

    // ===== Constructors =====
    public Permission() {
    }

    public Permission(Integer permissionID, String permissionName, String moTa, List<RolePermission> rolePermissions) {
        this.PermissionID = permissionID;
        this.PermissionName = permissionName;
        this.MoTa = moTa;
        this.rolePermissions = rolePermissions;
    }

    // ===== Getters & Setters =====
    public Integer getPermissionID() {
        return PermissionID;
    }

    public void setPermissionID(Integer permissionID) {
        this.PermissionID = permissionID;
    }

    public String getPermissionName() {
        return PermissionName;
    }

    public void setPermissionName(String permissionName) {
        this.PermissionName = permissionName;
    }

    public String getMoTa() {
        return MoTa;
    }

    public void setMoTa(String moTa) {
        this.MoTa = moTa;
    }

    public List<RolePermission> getRolePermissions() {
        return rolePermissions;
    }

    public void setRolePermissions(List<RolePermission> rolePermissions) {
        this.rolePermissions = rolePermissions;
    }
}
