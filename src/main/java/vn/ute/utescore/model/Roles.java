package vn.ute.utescore.model;


import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "Roles")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class Roles {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer RoleID;

    @Column(columnDefinition = "NVARCHAR(50)")
    private String RoleName;

    @Column(columnDefinition = "NVARCHAR(255)")
    private String MoTa;

    @OneToMany(mappedBy = "role", cascade = CascadeType.ALL)
    private List<NhanVien> nhanViens;

    @OneToMany(mappedBy = "role", cascade = CascadeType.ALL)
    private List<TaiKhoan> taiKhoans;

    @OneToMany(mappedBy = "role", cascade = CascadeType.ALL)
    private List<RolePermission> rolePermissions;
}
