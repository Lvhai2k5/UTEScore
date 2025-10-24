package vn.ute.utescore.model;


import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "SystemConfigurations")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class SystemConfigurations {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer ID;

    @Column(name = "configKey", columnDefinition = "NVARCHAR(100)")
    private String configKey;

    @Lob
    @Column(name = "configValue")
    private String configValue;

    @Column(name = "configType", columnDefinition = "NVARCHAR(50)")
    private String configType;

    @Lob
    private String Description;

    private Boolean isActive;

    @Column(name = "createdAt")
    private LocalDateTime createdAt;

    @Column(name = "updatedAt")
    private LocalDateTime updatedAt;
}
