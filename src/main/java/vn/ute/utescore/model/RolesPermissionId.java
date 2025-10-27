package vn.ute.utescore.model;

import jakarta.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
public class RolesPermissionId implements Serializable {

    private static final long serialVersionUID = 1L;
	private Integer RoleID;
    private Integer PermissionID;

    // ===== Constructors =====
    public RolesPermissionId() {
    }

    public RolesPermissionId(Integer roleID, Integer permissionID) {
        this.RoleID = roleID;
        this.PermissionID = permissionID;
    }

    // ===== Getters & Setters =====
    public Integer getRoleID() {
        return RoleID;
    }

    public void setRoleID(Integer roleID) {
        this.RoleID = roleID;
    }

    public Integer getPermissionID() {
        return PermissionID;
    }

    public void setPermissionID(Integer permissionID) {
        this.PermissionID = permissionID;
    }
}
