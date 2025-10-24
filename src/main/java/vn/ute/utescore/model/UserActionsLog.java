package vn.ute.utescore.model;


import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "UserActionsLog")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class UserActionsLog {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer LogID;

    @ManyToOne @JoinColumn(name = "UserID")
    private NhanVien user;

    @Column(columnDefinition = "NVARCHAR(255)")
    private String ActionType;

    @Column(columnDefinition = "NVARCHAR(255)")
    private String Reason;

    @ManyToOne @JoinColumn(name = "PerformedBy")
    private NhanVien performedBy;

    private LocalDateTime CreateAt;
}
