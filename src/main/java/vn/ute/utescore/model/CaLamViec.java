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
@Table(name = "CaLamViec")
public class CaLamViec {
@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
private Integer caId;

@ManyToOne(fetch = FetchType.LAZY)
@JoinColumn(name = "nhanVienId")
private NhanVien nhanVien;

private LocalDateTime ngayLam;

@ManyToOne(fetch = FetchType.LAZY)
@JoinColumn(name = "maCaLam")
private CaLam caLam;

private Integer tongDonXuLy;
private BigDecimal tongDoanhThu;
}
