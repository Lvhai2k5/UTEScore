package vn.ute.utescore.model;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "Camera")
public class Camera {
@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
private Integer cameraId;

@Column(length = 50)
private String tenCamera;

@ManyToOne(fetch = FetchType.LAZY)
@JoinColumn(name = "sanId")
private SanBong san;

@Column(length = 50)
private String ip;

@Column(length = 30)
private String trangThai; // 'Hoạt động', 'Mất kết nối', 'Bảo trì', 'Tạm ngưng'

@Column(length = 255)
private String fileMoPhong;

@ManyToOne(fetch = FetchType.LAZY)
@JoinColumn(name = "nhanVienPhuTrach")
private NhanVien nhanVienPhuTrach;

@Column(length = 255)
private String ghiChu;

private LocalDateTime ngayTao;
private LocalDateTime ngayCapNhat;
}
