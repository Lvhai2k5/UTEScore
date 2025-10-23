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
@Table(name = "DanhGiaDonHang")
public class DanhGiaDonHang {
@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
private Integer maDanhGia;

@ManyToOne(fetch = FetchType.LAZY)
@JoinColumn(name = "maDonDat")
private ThueSan donDat;

@ManyToOne(fetch = FetchType.LAZY)
@JoinColumn(name = "maKhachHang")
private KhachHang khachHang;

private Integer rating;

@Column(length = 255)
private String noiDung;

private LocalDateTime ngayDanhGia;

@Column(length = 255)
private String trangThaiPhanHoi;

@ManyToOne(fetch = FetchType.LAZY)
@JoinColumn(name = "maNhanVienPhanHoi")
private NhanVien nhanVienPhanHoi;
}
