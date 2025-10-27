package vn.ute.utescore.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "SystemConfigurations")
public class SystemConfigurations {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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

    // ===== Constructors =====
    public SystemConfigurations() {
    }

    public SystemConfigurations(Integer ID, String configKey, String configValue,
                                String configType, String description, Boolean isActive,
                                LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.ID = ID;
        this.configKey = configKey;
        this.configValue = configValue;
        this.configType = configType;
        this.Description = description;
        this.isActive = isActive;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // ===== Getters & Setters =====
    public Integer getID() {
        return ID;
    }

    public void setID(Integer ID) {
        this.ID = ID;
    }

    public String getConfigKey() {
        return configKey;
    }

    public void setConfigKey(String configKey) {
        this.configKey = configKey;
    }

    public String getConfigValue() {
        return configValue;
    }

    public void setConfigValue(String configValue) {
        this.configValue = configValue;
    }

    public String getConfigType() {
        return configType;
    }

    public void setConfigType(String configType) {
        this.configType = configType;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        this.Description = description;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
