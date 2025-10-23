package vn.ute.utescore.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.*;

@Entity
@Table(name = "LichSuCamera")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class LichSuCamera {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer lichSuID;

    @ManyToOne @JoinColumn(name = "cameraID")
    private Camera camera;

    @ManyToOne @JoinColumn(name = "nhanVienID")
    private NhanVien nhanVien;

    @Column(length = 30)
    private String trangThaiCu;

    @Column(length = 30)
    private String trangThaiMoi;

    private LocalDateTime thoiGianCapNhat;

    @Column(length = 255)
    private String ghiChu;
}
