package vn.ute.utescore.model;


import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "Permission")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class Permission {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer PermissionID;

    @Column(columnDefinition = "NVARCHAR(255)")
    private String PermissionName;

    @Column(columnDefinition = "NVARCHAR(255)")
    private String MoTa;

    @OneToMany(mappedBy = "permission", cascade = CascadeType.ALL)
    private List<RolePermission> rolePermissions;
}
