package vn.ute.utescore.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.*;

@Entity
@Table(name = "NhanVien")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class NhanVien {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer userID;

    @Column(nullable = false, length = 50)
    private String fullName;

    @Column(nullable = false, unique = true, length = 100)
    private String email;

    @Column(length = 11)
    private String phone;

    @Column(nullable = false, length = 255)
    private String passwordHash;

    @ManyToOne @JoinColumn(name = "roleID")
    private Roles role;

    @Column(length = 20)
    private String status; // Hoạt động, Ngưng

    private LocalDateTime createdAt;
    private LocalDateTime updateAt;
}
