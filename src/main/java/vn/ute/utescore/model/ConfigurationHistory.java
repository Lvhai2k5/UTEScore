package vn.ute.utescore.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.*;

@Entity
@Table(name = "configuration_history")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class ConfigurationHistory {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "config_key", length = 100)
    private String configKey;

    @Lob
    private String oldValue;

    @Lob
    private String newValue;

    @Column(length = 255)
    private String changedBy;

    @Lob
    private String changeReason;

    private LocalDateTime createdAt;
}
