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
@Table(name = "ThueSan")
public class ThueSan {
@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
private Integer maDonDat;

@ManyToOne(fetch = FetchType.LAZY)
@JoinColumn(name = "maKhachHang")
private KhachHang khachHang;

@ManyToOne(fetch = FetchType.LAZY)
@JoinColumn(name = "maSan")
private SanBong san;

private LocalDateTime ngayThue;
private LocalTime khungGioBatDau;
private LocalTime khungGioKetThuc;

private BigDecimal tongTien;
private BigDecimal tienCocBatBuoc;
private BigDecimal soTienConLai;

private LocalDateTime ngayTao;
private LocalDateTime hanGiuCho;

@Column(length = 255)
private String ghiChu;
}
