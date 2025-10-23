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
@Table(name = "ConfigurationHistory")
public class ConfigurationHistory {
@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
private Integer id;

@Column(length = 100, nullable = false)
private String configKey;

@Lob
private String oldValue;

@Lob
private String newValue;

@Column(length = 255)
private String changedBy;

@Column(length = 255)
private String changeReason;

private LocalDateTime createdAt;
}
