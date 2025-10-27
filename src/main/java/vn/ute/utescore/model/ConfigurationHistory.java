package vn.ute.utescore.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "ConfigurationHistory")
public class ConfigurationHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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

    // ===== Constructors =====
    public ConfigurationHistory() {
    }

    public ConfigurationHistory(Integer ID, String configKey, String oldValue, String newValue,
                                String changedBy, String changeReason, LocalDateTime createdAt) {
        this.ID = ID;
        this.configKey = configKey;
        this.oldValue = oldValue;
        this.newValue = newValue;
        this.changedBy = changedBy;
        this.changeReason = changeReason;
        this.createdAt = createdAt;
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

    public String getOldValue() {
        return oldValue;
    }

    public void setOldValue(String oldValue) {
        this.oldValue = oldValue;
    }

    public String getNewValue() {
        return newValue;
    }

    public void setNewValue(String newValue) {
        this.newValue = newValue;
    }

    public String getChangedBy() {
        return changedBy;
    }

    public void setChangedBy(String changedBy) {
        this.changedBy = changedBy;
    }

    public String getChangeReason() {
        return changeReason;
    }

    public void setChangeReason(String changeReason) {
        this.changeReason = changeReason;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

   
}
