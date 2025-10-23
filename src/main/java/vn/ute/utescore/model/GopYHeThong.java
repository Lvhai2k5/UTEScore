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
@Table(name = "GopYHeThong")
public class GopYHeThong {
@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
private Integer maGopY;

@ManyToOne(fetch = FetchType.LAZY)
@JoinColumn(name = "maKhachHang")
private KhachHang khachHang;

@Column(length = 50)
private String loaiGopY; // 'Góp ý chung', 'Góp ý sân'

@ManyToOne(fetch = FetchType.LAZY)
@JoinColumn(name = "maSan")
private SanBong san;

@Column(length = 255)
private String noiDung;

private LocalDateTime ngayGopY;

@Column(length = 50)
private String trangThaiXuLy; // 'Chưa xử lý', 'Đã phản hồi', 'Đã đóng'

@Column(length = 255)
private String phanHoiTuHeThong;

@ManyToOne(fetch = FetchType.LAZY)
@JoinColumn(name = "maNhanVienXuLy")
private NhanVien nhanVienXuLy;
}
