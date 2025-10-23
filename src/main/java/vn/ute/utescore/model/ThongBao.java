package vn.ute.utescore.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.*;

@Entity
@Table(name = "ThongBao")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class ThongBao {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer maThongBao;

    @ManyToOne @JoinColumn(name = "maKhachHang")
    private KhachHang khachHang;

    @Column(length = 255)
    private String tieuDe;

    @Column(length = 255)
    private String noiDung;

    @Column(length = 50)
    private String loaiThongBao;

    private LocalDateTime ngayGui;

    @Column(length = 50)
    private String trangThai; // Đã gửi, Đã Xóa, Chưa đọc
}
