package vn.ute.utescore.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.*;

@Entity
@Table(name = "ThanhToan")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class ThanhToan {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer maThanhToan;

    @ManyToOne @JoinColumn(name = "maDonDat", nullable = false)
    private ThueSan donDat;

    private Double soTienNhan;

    @Column(nullable = false, length = 50)
    private String phuongThuc; // VNPay, Tiền mặt

    @Column(nullable = false, length = 50)
    private String loaiThanhToan; // Đặt cọc, Còn lại, Hoàn tiền, Giảm điểm

    @Column(nullable = false, length = 50)
    private String trangThaiThanhToan; // Thành công, Thất bại, Chờ

    private Integer soDiemSuDung;
    private Double giaTriDiemGiam;
    private Integer soDiemCongThem;

    private LocalDateTime ngayThanhToan;

    @Column(length = 255)
    private String maGiaoDich;

    @Column(length = 255)
    private String ghiChu;
}
