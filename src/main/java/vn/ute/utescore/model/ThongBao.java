package vn.ute.utescore.model;


import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "ThongBao")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class ThongBao {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer MaThongBao;

    @ManyToOne @JoinColumn(name = "MaKhachHang")
    private KhachHang khachHang;

    @Column(columnDefinition = "NVARCHAR(255)")
    private String TieuDe;

    @Column(columnDefinition = "NVARCHAR(255)")
    private String NoiDung;

    @Column(columnDefinition = "NVARCHAR(50)")
    private String LoaiThongBao;

    private LocalDateTime NgayGui;

    @Column(columnDefinition = "NVARCHAR(50)")
    private String TrangThai;
}
