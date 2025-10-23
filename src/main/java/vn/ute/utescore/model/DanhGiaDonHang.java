package vn.ute.utescore.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.*;

@Entity
@Table(name = "DanhGiaDonHang")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class DanhGiaDonHang {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer maDanhGia;

    @ManyToOne @JoinColumn(name = "maDonDat")
    private ThueSan donDat;

    @ManyToOne @JoinColumn(name = "maKhachHang")
    private KhachHang khachHang;

    private Integer rating; // thang điểm

    @Column(length = 255)
    private String noiDung;

    private LocalDateTime ngayDanhGia;

    @Column(length = 255)
    private String trangThaiPhanHoi;

    @ManyToOne @JoinColumn(name = "maNhanVienPhanHoi")
    private NhanVien nhanVienPhanHoi;
}
