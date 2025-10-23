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
@Table(name = "TinTuc")
public class TinTuc {
@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
private Integer tinId;

@Column(length = 255)
private String tieuDe;

@Column(length = 255)
private String noiDung;

@Lob
private byte[] hinhAnh;

private LocalDateTime ngayDang;

@Column(length = 50)
private String trangThai; // 'Hiển thị', 'Ẩn'
}
