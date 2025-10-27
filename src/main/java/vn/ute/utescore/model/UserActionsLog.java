package vn.ute.utescore.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "UserActionsLog")
public class UserActionsLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer LogID;

    @ManyToOne
    @JoinColumn(name = "UserID")
    private NhanVien user;

    @Column(columnDefinition = "NVARCHAR(255)")
    private String ActionType;

    @Column(columnDefinition = "NVARCHAR(255)")
    private String Reason;

    @ManyToOne
    @JoinColumn(name = "PerformedBy")
    private NhanVien performedBy;

    private LocalDateTime CreateAt;

    // ===== Constructors =====
    public UserActionsLog() {
    }

    public UserActionsLog(Integer logID, NhanVien user, String actionType,
                          String reason, NhanVien performedBy, LocalDateTime createAt) {
        this.LogID = logID;
        this.user = user;
        this.ActionType = actionType;
        this.Reason = reason;
        this.performedBy = performedBy;
        this.CreateAt = createAt;
    }

    // ===== Getters & Setters =====
    public Integer getLogID() {
        return LogID;
    }

    public void setLogID(Integer logID) {
        this.LogID = logID;
    }

    public NhanVien getUser() {
        return user;
    }

    public void setUser(NhanVien user) {
        this.user = user;
    }

    public String getActionType() {
        return ActionType;
    }

    public void setActionType(String actionType) {
        this.ActionType = actionType;
    }

    public String getReason() {
        return Reason;
    }

    public void setReason(String reason) {
        this.Reason = reason;
    }

    public NhanVien getPerformedBy() {
        return performedBy;
    }

    public void setPerformedBy(NhanVien performedBy) {
        this.performedBy = performedBy;
    }

    public LocalDateTime getCreateAt() {
        return CreateAt;
    }

    public void setCreateAt(LocalDateTime createAt) {
        this.CreateAt = createAt;
    }
}
