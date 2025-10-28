package vn.ute.utescore.dto;

public class RoleDTO {
    private Integer roleID;
    private String roleName;
    private String moTa;
    
    public RoleDTO() {
    }
    
    public RoleDTO(Integer roleID, String roleName, String moTa) {
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
}
