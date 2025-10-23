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
@Table(name = "SuCo")
public class SuCo {
@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
private Integer suCoId;

@ManyToOne(fetch = FetchType.LAZY)
@JoinColumn(name = "maDon")
private ThueSan donDat;

@Column(length = 255)
private String moTa;

@Column(length = 50)
private String loaiSuCo;

@ManyToOne(fetch = FetchType.LAZY)
@JoinColumn(name = "nhanVienId")
private NhanVien nhanVien;

private LocalDateTime thoiGianBaoCao;

@Column(length = 50)
private String trangThai; // 'Đang xử lý', 'Hoàn tất', 'Đã hủy'

private LocalDateTime thoiGianHoanThanh;
}
