package vn.ute.utescore.model;


import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "LichSuCamera")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class LichSuCamera {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer LichSuID;

    @ManyToOne @JoinColumn(name = "CameraID")
    private Camera camera;

    @ManyToOne @JoinColumn(name = "NhanVienID")
    private NhanVien nhanVien;

    @Column(columnDefinition = "NVARCHAR(30)")
    private String TrangThaiCu;

    @Column(columnDefinition = "NVARCHAR(30)")
    private String TrangThaiMoi;

    private LocalDateTime ThoiGianCapNhat;

    @Column(columnDefinition = "NVARCHAR(255)")
    private String GhiChu;
}
