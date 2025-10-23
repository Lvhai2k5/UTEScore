package vn.ute.utescore.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "TaiKhoan")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class TaiKhoan {
    @Id
    @Column(length = 100)
    private String email; // Giả định email là duy nhất

    @Column(length = 11)
    private String soDienThoai;

    @Column(length = 100, nullable = false)
    private String matKhau;

    @ManyToOne @JoinColumn(name = "roleID")
    private Roles role;

    @Column(length = 20)
    private String trangThai; // Hoạt động, Ngưng
}
