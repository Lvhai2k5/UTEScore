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
@Table(name = "KhachHang")
public class KhachHang {
@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
private Integer maKhachHang;

@Column(length = 50)
private String hoTen;

@Column(length = 11)
private String soDienThoai;

@Column(length = 100)
private String email;

@Lob
private byte[] anhDaiDien;

private Integer diemTichLuy;

@Column(length = 20)
private String trangThai; // 'Hoạt động', 'Ngưng'

private LocalDateTime ngayDangKy;

private Boolean isDeleted = false;
}
