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
@Table(name = "Roles")
public class Roles {
@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
private Integer roleId;

@Column(length = 50)
private String roleName;

@Column(length = 255)
private String moTa;
}
