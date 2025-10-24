package vn.ute.utescore.model;


import jakarta.persistence.*;
import lombok.*;


@Entity
@Table(name = "TaiKhoan")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class TaiKhoan {
    @Id
    @Column(columnDefinition = "NVARCHAR(100)")
    private String Email;

    @Column(columnDefinition = "NVARCHAR(11)")
    private String SoDienThoai;

    @Column(columnDefinition = "NVARCHAR(100)")
    private String MatKhau;

    @ManyToOne @JoinColumn(name = "RoleID")
    private Roles role;

    @Column(columnDefinition = "NVARCHAR(20)")
    private String TrangThai;
}
