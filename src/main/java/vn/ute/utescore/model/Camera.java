package vn.ute.utescore.model;


import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "Camera")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class Camera {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer CameraID;

    @Column(columnDefinition = "NVARCHAR(50)")
    private String TenCamera;

    @ManyToOne @JoinColumn(name = "SanID")
    private SanBong sanBong;

    @Column(columnDefinition = "NVARCHAR(50)")
    private String IP;

    @Column(columnDefinition = "NVARCHAR(30)")
    private String TrangThai;

    @Column(columnDefinition = "NVARCHAR(255)")
    private String FileMoPhong;

    @ManyToOne @JoinColumn(name = "NhanVienPhuTrach")
    private NhanVien nhanVienPhuTrach;

    @Column(columnDefinition = "NVARCHAR(255)")
    private String GhiChu;

    private LocalDateTime NgayTao;
    private LocalDateTime NgayCapNhat;

    @OneToMany(mappedBy = "camera", cascade = CascadeType.ALL)
    private List<LichSuCamera> lichSuCameras;
}
