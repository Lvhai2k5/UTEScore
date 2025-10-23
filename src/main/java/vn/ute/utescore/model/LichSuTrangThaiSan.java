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
@Table(name = "LichSuTrangThaiSan")
public class LichSuTrangThaiSan {
@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
private Integer lichSuId;

@ManyToOne(fetch = FetchType.LAZY)
@JoinColumn(name = "sanId")
private SanBong san;

@Column(length = 50)
private String trangThaiCu;

@Column(length = 50)
private String trangThaiMoi;

private LocalDateTime thoiGianThayDoi;

@ManyToOne(fetch = FetchType.LAZY)
@JoinColumn(name = "nguoiThucHien")
private NhanVien nguoiThucHien;

@Column(length = 255)
private String ghiChu;
}
