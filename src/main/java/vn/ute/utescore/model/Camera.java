package vn.ute.utescore.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.*;

@Entity
@Table(name = "Camera")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class Camera {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer cameraID;

    @Column(length = 50)
    private String tenCamera;

    @ManyToOne @JoinColumn(name = "sanID")
    private SanBong sanBong;

    @Column(length = 50)
    private String ip;

    @Column(length = 30)
    private String trangThai; // Hoạt động, Mất kết nối, Bảo trì, Tạm ngưng

    @Column(length = 255)
    private String fileMoPhong;

    @ManyToOne @JoinColumn(name = "nhanVienPhuTrach")
    private NhanVien nhanVienPhuTrach;

    @Column(length = 255)
    private String ghiChu;

    private LocalDateTime ngayTao;
    private LocalDateTime ngayCapNhat;
}
