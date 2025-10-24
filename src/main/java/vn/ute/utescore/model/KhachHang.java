package vn.ute.utescore.model;


import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "KhachHang")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class KhachHang {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer MaKhachHang;

    @Column(columnDefinition = "NVARCHAR(50)")
    private String HoTen;

    @Column(columnDefinition = "NVARCHAR(11)")
    private String SoDienThoai;

    @Column(columnDefinition = "NVARCHAR(100)")
    private String Email;

    @Lob
    private byte[] AnhDaiDien;

    private Integer DiemTichLuy;

    @Column(columnDefinition = "NVARCHAR(20)")
    private String TrangThai;

    private LocalDateTime NgayDangKy;
    private Boolean IsDeleted;

    @OneToMany(mappedBy = "khachHang", cascade = CascadeType.ALL)
    private List<ThueSan> thueSans;

    @OneToMany(mappedBy = "khachHang", cascade = CascadeType.ALL)
    private List<ThongBao> thongBaos;

    @OneToMany(mappedBy = "khachHang", cascade = CascadeType.ALL)
    private List<GopYHeThong> gopYHeThongs;

    @OneToMany(mappedBy = "khachHang", cascade = CascadeType.ALL)
    private List<DanhGiaDonHang> danhGiaDonHangs;
}
