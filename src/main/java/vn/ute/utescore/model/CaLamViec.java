package vn.ute.utescore.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.*;

@Entity
@Table(name = "CaLamViec")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class CaLamViec {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer caID;

    @ManyToOne @JoinColumn(name = "nhanVienID")
    private NhanVien nhanVien;

    private LocalDateTime ngayLam;

    @ManyToOne @JoinColumn(name = "maCaLam")
    private CaLam caLam;

    private Integer tongDonXuLy;
    private Double tongDoanhThu;
}
