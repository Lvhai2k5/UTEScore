package vn.ute.utescore.model;


import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Entity
@Table(name = "ThueSan")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class ThueSan {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer MaDonDat;

    @ManyToOne @JoinColumn(name = "MaKhachHang")
    private KhachHang khachHang;

    @ManyToOne @JoinColumn(name = "MaSan")
    private SanBong sanBong;

    private LocalDateTime NgayThue;
    private LocalTime KhungGioBatDau;
    private LocalTime KhungGioKetThuc;

    private BigDecimal TongTien;
    private BigDecimal TienCocBatBuoc;

    @Column(insertable = false, updatable = false)
    private BigDecimal SoTienConLai;

    private LocalDateTime NgayTao;
    private LocalDateTime HanGiuCho;

    @Column(columnDefinition = "NVARCHAR(255)")
    private String GhiChu;

    @OneToMany(mappedBy = "thueSan", cascade = CascadeType.ALL)
    private List<ThanhToan> thanhToans;

    @OneToMany(mappedBy = "thueSan", cascade = CascadeType.ALL)
    private List<DanhGiaDonHang> danhGiaDonHangs;

    @OneToMany(mappedBy = "thueSan", cascade = CascadeType.ALL)
    private List<SuCo> suCos;
}
