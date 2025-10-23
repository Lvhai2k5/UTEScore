package vn.ute.utescore.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.*;

@Entity
@Table(name = "ThueSan")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class ThueSan {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer maDonDat;

    @ManyToOne @JoinColumn(name = "maKhachHang", nullable = false)
    private KhachHang khachHang;

    @ManyToOne @JoinColumn(name = "maSan", nullable = false)
    private SanBong sanBong;

    private LocalDateTime ngayThue;
    private LocalTime khungGioBatDau;
    private LocalTime khungGioKetThuc;

    private Double tongTien;
    private Double tienCocBatBuoc;
    private Double soTienConLai;

    private LocalDateTime ngayTao;
    private LocalDateTime hanGiuCho;

    @Column(length = 255)
    private String ghiChu;
}
