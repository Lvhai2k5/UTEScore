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
@Table(name = "Permission")
public class Permission {
@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
private Integer permissionId;

@Column(length = 100)
private String permissionName;

@Column(length = 255)
private String moTa;
}
