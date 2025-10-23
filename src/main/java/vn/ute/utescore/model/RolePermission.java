package vn.ute.utescore.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "RolePermission")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class RolePermission {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne @JoinColumn(name = "roleID")
    private Roles role;

    @ManyToOne @JoinColumn(name = "permissionID")
    private Permission permission;
}
