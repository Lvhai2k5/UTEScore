package vn.ute.utescore.model;


import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "NhanVien")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class NhanVien {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer UserID;

    @Column(columnDefinition = "NVARCHAR(50)")
    private String FullName;

    @Column(columnDefinition = "NVARCHAR(100)")
    private String Email;

    @Column(columnDefinition = "NVARCHAR(11)")
    private String Phone;

    @ManyToOne @JoinColumn(name = "RoleID")
    private Roles role;

    @Column(columnDefinition = "NVARCHAR(20)")
    private String Status;

    private LocalDateTime CreatedAt;
    private LocalDateTime UpdateAt;

    @OneToMany(mappedBy = "nhanVien", cascade = CascadeType.ALL)
    private List<BaoTri> baoTris;

    @OneToMany(mappedBy = "nhanVien", cascade = CascadeType.ALL)
    private List<SuCo> suCos;

    @OneToMany(mappedBy = "nhanVienPhuTrach", cascade = CascadeType.ALL)
    private List<Camera> cameras;

    @OneToMany(mappedBy = "nhanVien", cascade = CascadeType.ALL)
    private List<LichSuCamera> lichSuCameras;

    @OneToMany(mappedBy = "nguoiThucHien", cascade = CascadeType.ALL)
    private List<LichSuTrangThaiSan> lichSuTrangThais;

    @OneToMany(mappedBy = "nhanVienXuLy", cascade = CascadeType.ALL)
    private List<GopYHeThong> gopYXuLys;

    @OneToMany(mappedBy = "nhanVienPhanHoi", cascade = CascadeType.ALL)
    private List<DanhGiaDonHang> danhGiaPhanHois;

    @OneToMany(mappedBy = "nhanVien", cascade = CascadeType.ALL)
    private List<CaLamViec> caLamViecs;
}
