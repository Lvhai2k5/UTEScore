package vn.ute.utescore.model;


import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "ThanhToan")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class ThanhToan {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer MaThanhToan;

    @ManyToOne @JoinColumn(name = "MaDonDat")
    private ThueSan thueSan;

    private BigDecimal SoTienNhan;

    @Column(columnDefinition = "NVARCHAR(50)", nullable = false)
    private String PhuongThuc;

    @Column(columnDefinition = "NVARCHAR(50)", nullable = false)
    private String LoaiThanhToan;

    @Column(columnDefinition = "NVARCHAR(50)", nullable = false)
    private String TrangThaiThanhToan;

    private Integer SoDiemSuDung;
    private BigDecimal GiaTriDiemGiam;
    private Integer SoDiemCongThem;

    private LocalDateTime NgayThanhToan;

    @Column(columnDefinition = "NVARCHAR(255)")
    private String MaGiaoDich;

    @Column(columnDefinition = "NVARCHAR(255)")
    private String GhiChu;
}
