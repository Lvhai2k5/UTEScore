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
@Table(name = "GiaThue")
public class GiaThue {
@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
private Integer maBangGia;

@Column(length = 20, nullable = false)
private String loaiSan; // '5 người', '7 người', '11 người'

private LocalTime khungGioBatDau;
private LocalTime khungGioKetThuc;

@Column(length = 255)
private String moTa;

private BigDecimal giaThue;
private LocalDateTime ngayApDung;

@Column(length = 50)
private String trangThai; // 'Đang áp dụng', 'Hết hiệu lực'
}
