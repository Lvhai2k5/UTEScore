package vn.ute.utescore.model;


import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Entity
@Table(name = "SanBong")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class SanBong {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer MaSan;

    @Column(columnDefinition = "NVARCHAR(20)", nullable = false)
    private String TenSan;

    @Column(columnDefinition = "NVARCHAR(20)", nullable = false)
    private String LoaiSan;

    @Column(columnDefinition = "NVARCHAR(50)")
    private String KhuVuc;

    @Column(columnDefinition = "NVARCHAR(255)")
    private String DuongDanGGMap;

    private LocalTime GioMoCua;
    private LocalTime GioDongCua;

    @Column(columnDefinition = "NVARCHAR(255)")
    private String MoTa;

    @Lob
    private byte[] HinhAnh;

    @Column(columnDefinition = "NVARCHAR(50)")
    private String TrangThai;

    private LocalDateTime NgayTao;
    private LocalDateTime NgayCapNhat;

    @OneToMany(mappedBy = "sanBong", cascade = CascadeType.ALL)
    private List<ThueSan> thueSans;

    @OneToMany(mappedBy = "sanBong", cascade = CascadeType.ALL)
    private List<BaoTri> baoTris;

    @OneToMany(mappedBy = "sanBong", cascade = CascadeType.ALL)
    private List<TinhNangSan> tinhNangSans;

    @OneToMany(mappedBy = "sanBong", cascade = CascadeType.ALL)
    private List<GopYHeThong> gopYHeThongs;

    @OneToMany(mappedBy = "sanBong", cascade = CascadeType.ALL)
    private List<Camera> cameras;

    @OneToMany(mappedBy = "sanBong", cascade = CascadeType.ALL)
    private List<LichSuTrangThaiSan> lichSuTrangThaiSans;
}
