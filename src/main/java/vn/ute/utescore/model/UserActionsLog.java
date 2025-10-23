package vn.ute.utescore.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.*;

@Entity
@Table(name = "UserActionsLog")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class UserActionsLog {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer logID;

    @ManyToOne @JoinColumn(name = "userID")
    private NhanVien user; // Người bị tác động

    @Column(length = 100)
    private String actionType;

    @Column(length = 255)
    private String reason;

    @ManyToOne @JoinColumn(name = "performedBy")
    private NhanVien performedBy; // Người thực hiện

    private LocalDateTime createAt;
}
