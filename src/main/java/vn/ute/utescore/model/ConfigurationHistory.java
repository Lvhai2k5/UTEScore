package vn.ute.utescore.model;


import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "ConfigurationHistory")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class ConfigurationHistory {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer ID;

    @Column(name = "configKey", columnDefinition = "NVARCHAR(100)")
    private String configKey;

    @Lob
    private String oldValue;

    @Lob
    private String newValue;

    @Column(columnDefinition = "NVARCHAR(255)")
    private String changedBy;

    @Column(columnDefinition = "NVARCHAR(255)")
    private String changeReason;

    @Column(name = "createdAt")
    private LocalDateTime createdAt;
}
