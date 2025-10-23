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
@Table(name = "ThongBao")
public class ThongBao {
@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
private Integer maThongBao;

@ManyToOne(fetch = FetchType.LAZY)
@JoinColumn(name = "maKhachHang")
private KhachHang khachHang;

@Column(length = 255)
private String tieuDe;

@Column(length = 255)
private String noiDung;

@Column(length = 50)
private String loaiThongBao;

private LocalDateTime ngayGui;

@Column(length = 50)
private String trangThai; // 'Đã gửi', 'Đã Xóa', 'Chưa đọc' (default)
}
