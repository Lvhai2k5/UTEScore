package vn.ute.utescore.model;

import jakarta.persistence.*;

@Entity
@Table(name = "RolesPermission")
public class RolesPermission {

    @EmbeddedId
    private RolesPermissionId id;

    @ManyToOne
    @MapsId("RoleID")
    @JoinColumn(name = "RoleID")
    private Roles role;

    @ManyToOne
    @MapsId("PermissionID")
    @JoinColumn(name = "PermissionID")
    private Permission permission;

    // ===== Constructors =====
    public RolesPermission() {
    }

    public RolesPermission(RolesPermissionId id, Roles role, Permission permission) {
        this.id = id;
        this.role = role;
        this.permission = permission;
    }

    // ===== Getters & Setters =====
    public RolesPermissionId getId() {
        return id;
    }

    public void setId(RolesPermissionId id) {
        this.id = id;
    }

    public Roles getRole() {
        return role;
    }

    public void setRole(Roles role) {
        this.role = role;
    }

    public Permission getPermission() {
        return permission;
    }

    public void setPermission(Permission permission) {
        this.permission = permission;
    }
}
