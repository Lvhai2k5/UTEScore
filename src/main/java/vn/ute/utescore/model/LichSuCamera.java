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
@Table(name = "LichSuCamera")
public class LichSuCamera {
@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
private Integer lichSuId;

@ManyToOne(fetch = FetchType.LAZY)
@JoinColumn(name = "cameraId")
private Camera camera;

@ManyToOne(fetch = FetchType.LAZY)
@JoinColumn(name = "nhanVienId")
private NhanVien nhanVien;

@Column(length = 30)
private String trangThaiCu;

@Column(length = 30)
private String trangThaiMoi;

private LocalDateTime thoiGianCapNhat;

@Column(length = 255)
private String ghiChu;
}
