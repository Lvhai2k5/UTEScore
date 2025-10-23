package vn.ute.utescore.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.*;

@Entity
@Table(name = "system_configurations")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class SystemConfigurations {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "config_key", unique = true, nullable = false, length = 100)
    private String configKey;

    @Lob
    @Column(name = "config_value", nullable = false)
    private String configValue;

    @Column(name = "config_type", length = 50)
    private String configType; // payment, booking, notification, system

    @Lob
    private String description;

    private Boolean isActive;

    private java.time.LocalDateTime createdAt;
    private java.time.LocalDateTime updatedAt;
}
