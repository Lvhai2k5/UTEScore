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
@Table(name = "SanBong")
public class SanBong {
@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
private Integer maSan;

@Column(length = 20, nullable = false)
private String tenSan;

@Column(length = 20, nullable = false)
private String loaiSan; // '5 người', '7 người', '11 người'

@Column(length = 50)
private String khuVuc;

@Column(length = 255)
private String duongDanGgMap;

private LocalTime gioMoCua;
private LocalTime gioDongCua;

@Column(length = 255)
private String moTa;

@Lob
private byte[] hinhAnh;

@Column(length = 50)
private String trangThai; // 'Hoạt động', 'Bảo trì', 'Ngưng sử dụng'

private LocalDateTime ngayTao;
private LocalDateTime ngayCapNhat;
}
