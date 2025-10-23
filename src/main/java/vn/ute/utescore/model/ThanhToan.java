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
@Table(name = "ThanhToan")
public class ThanhToan {
@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
private Integer maThanhToan;

@ManyToOne(fetch = FetchType.LAZY)
@JoinColumn(name = "maDonDat")
private ThueSan donDat;

private BigDecimal soTienNhan;

@Column(length = 50, nullable = false)
private String phuongThuc; // VNPay, Tiền mặt

@Column(length = 50, nullable = false)
private String loaiThanhToan; // Đặt cọc, Còn lại, Hoàn tiền, Giảm điểm

@Column(length = 50, nullable = false)
private String trangThaiThanhToan; // Thành công, Thất bại, Chờ

private Integer soDiemSuDung;
private BigDecimal giaTriDiemGiam;
private Integer soDiemCongThem;

private LocalDateTime ngayThanhToan;

@Column(length = 255)
private String maGiaoDich;

@Column(length = 255)
private String ghiChu;
}
