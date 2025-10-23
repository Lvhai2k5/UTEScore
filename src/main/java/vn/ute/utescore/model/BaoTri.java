package vn.ute.utescore.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.*;

@Entity
@Table(name = "BaoTri")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class BaoTri {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer baoTriID;

    @ManyToOne @JoinColumn(name = "maSan", nullable = false)
    private SanBong sanBong;

    @ManyToOne @JoinColumn(name = "nhanVienID")
    private NhanVien nhanVien;

    private LocalTime thoiGianBatDau;
    private LocalTime thoiGianKetThuc;
    private LocalDateTime ngayBaoTri;

    @Column(length = 255)
    private String lyDo;

    @Column(length = 50)
    private String trangThai; // Đang bảo trì, Hoàn tất, Đã hủy
}
