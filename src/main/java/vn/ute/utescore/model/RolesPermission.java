package vn.ute.utescore.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "RolesPermission")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class RolesPermission {
    @EmbeddedId
    private RolesPermissionId id;

    @ManyToOne @MapsId("RoleID")
    @JoinColumn(name = "RoleID")
    private Roles role;

    @ManyToOne @MapsId("PermissionID")
    @JoinColumn(name = "PermissionID")
    private Permission permission;
}
