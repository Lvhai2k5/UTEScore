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
@Table(name = "NhanVien")
public class NhanVien {
@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
private Integer userId;

@Column(length = 50)
private String fullName;

@Column(length = 100)
private String email;

@Column(length = 11)
private String phone;

@Column(length = 255)
private String passwordHash;

@ManyToOne(fetch = FetchType.LAZY)
@JoinColumn(name = "roleId")
private Roles role;

@Column(length = 20)
private String status; // 'Hoạt động', 'Ngưng'

private LocalDateTime createdAt;
private LocalDateTime updateAt;
}
