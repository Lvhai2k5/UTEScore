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
@Table(name = "TaiKhoan")
public class TaiKhoan {
@Id
@Column(length = 100)
private String email;

@Column(length = 11)
private String soDienThoai;

@Column(length = 100)
private String matKhau;

@ManyToOne(fetch = FetchType.LAZY)
@JoinColumn(name = "roleId")
private Roles role;

@Column(length = 20)
private String trangThai; // 'Hoạt động', 'Ngưng'

// Optional linking if needed
@OneToOne(fetch = FetchType.LAZY)
@JoinColumn(name = "userId")
private NhanVien nhanVien;

@OneToOne(fetch = FetchType.LAZY)
@JoinColumn(name = "maKhachHang")
private KhachHang khachHang;
}
