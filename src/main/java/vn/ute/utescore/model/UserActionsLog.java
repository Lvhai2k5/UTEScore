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
@Table(name = "UserActionsLog")
public class UserActionsLog {
@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
private Integer logId;

@ManyToOne(fetch = FetchType.LAZY)
@JoinColumn(name = "userId")
private NhanVien user;

@Column(length = 100)
private String actionType;

@Column(length = 255)
private String reason;

@ManyToOne(fetch = FetchType.LAZY)
@JoinColumn(name = "performedBy")
private NhanVien performedBy;

private LocalDateTime createAt;
}
