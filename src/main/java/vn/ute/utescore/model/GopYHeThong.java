package vn.ute.utescore.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.*;

@Entity
@Table(name = "GopYHeThong")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class GopYHeThong {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer maGopY;

    @ManyToOne @JoinColumn(name = "maKhachHang")
    private KhachHang khachHang;

    @Column(length = 50)
    private String loaiGopY; // Góp ý chung, Góp ý sân

    @ManyToOne @JoinColumn(name = "maSan")
    private SanBong sanBong;

    @Column(length = 255)
    private String noiDung;

    private LocalDateTime ngayGopY;

    @Column(length = 50)
    private String trangThaiXuLy; // Chưa xử lý, Đã phản hồi, Đã đóng

    @Column(length = 255)
    private String phanHoiTuHeThong;

    @ManyToOne @JoinColumn(name = "maNhanVienXuLy")
    private NhanVien nhanVienXuLy;
}
