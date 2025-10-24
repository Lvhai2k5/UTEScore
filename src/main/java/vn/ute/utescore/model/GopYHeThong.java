package vn.ute.utescore.model;


import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "GopYHeThong")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class GopYHeThong {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer MaGopY;

    @ManyToOne @JoinColumn(name = "MaKhachHang")
    private KhachHang khachHang;

    @Column(columnDefinition = "NVARCHAR(50)")
    private String LoaiGopY;

    @ManyToOne @JoinColumn(name = "MaSan")
    private SanBong sanBong;

    @Column(columnDefinition = "NVARCHAR(255)")
    private String NoiDung;

    private LocalDateTime NgayGopY;

    @Column(columnDefinition = "NVARCHAR(50)")
    private String TrangThaiXuLy;

    @Column(columnDefinition = "NVARCHAR(255)")
    private String PhanHoiTuHeThong;

    @ManyToOne @JoinColumn(name = "MaNhanVienXuLy")
    private NhanVien nhanVienXuLy;
}
