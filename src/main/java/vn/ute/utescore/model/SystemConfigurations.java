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
@Table(name = "SystemConfigurations")
public class SystemConfigurations {
@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
private Integer id;

@Column(length = 100, unique = true, nullable = false)
private String configKey;

@Lob
private String configValue;

@Column(length = 50, nullable = false)
private String configType; // 'payment', 'booking', 'notification', 'system'

@Lob
private String description;

private Boolean isActive = true;

private LocalDateTime createdAt;
private LocalDateTime updatedAt;
}
