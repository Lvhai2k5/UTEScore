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
@Table(name = "KhuyenMai")
public class KhuyenMai {
@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
private Integer khuyenMaiId;

@Column(length = 255)
private String tieuDe;

@Column(length = 255)
private String noiDung;

@Lob
private byte[] hinhAnh;

private LocalDateTime ngayBatDau;
private LocalDateTime ngayKetThuc;

@Column(length = 50)
private String trangThai; // 'Đang diễn ra', 'Sắp diễn ra', 'Đã kết thúc'
}
