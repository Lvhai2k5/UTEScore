package vn.ute.utescore.model;


import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "DanhGiaDonHang")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class DanhGiaDonHang {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer MaDanhGia;

    @ManyToOne @JoinColumn(name = "MaDonDat")
    private ThueSan thueSan;

    @ManyToOne @JoinColumn(name = "MaKhachHang")
    private KhachHang khachHang;

    private Integer Rating;

    @Column(columnDefinition = "NVARCHAR(255)")
    private String NoiDung;

    private LocalDateTime NgayDanhGia;

    @Column(columnDefinition = "NVARCHAR(255)")
    private String TrangThaiPhanHoi;

    @ManyToOne @JoinColumn(name = "MaNhanVienPhanHoi")
    private NhanVien nhanVienPhanHoi;
}
