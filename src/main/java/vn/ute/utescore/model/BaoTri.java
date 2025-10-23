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
@Table(name = "BaoTri")
public class BaoTri {
@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
private Integer baoTriId;

@ManyToOne(fetch = FetchType.LAZY)
@JoinColumn(name = "maSan")
private SanBong san;

@ManyToOne(fetch = FetchType.LAZY)
@JoinColumn(name = "nhanVienId")
private NhanVien nhanVien;

private LocalTime thoiGianBatDau;
private LocalTime thoiGianKetThuc;
private LocalDateTime ngayBaoTri;

@Column(length = 255)
private String lyDo;

@Column(length = 50)
private String trangThai; // 'Đang bảo trì', 'Hoàn tất', 'Đã hủy'
}
