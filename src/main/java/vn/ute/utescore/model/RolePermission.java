package vn.ute.utescore.model;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "RolePermission")
public class RolePermission {
@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
private Integer id;

@ManyToOne(fetch = FetchType.LAZY)
@JoinColumn(name = "roleId")
private Roles role;

@ManyToOne(fetch = FetchType.LAZY)
@JoinColumn(name = "permissionId")
private Permission permission;
}
