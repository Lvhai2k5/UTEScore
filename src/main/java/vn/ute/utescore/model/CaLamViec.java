package vn.ute.utescore.model;


import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "CaLamViec")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class CaLamViec {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer CaID;

    @ManyToOne @JoinColumn(name = "NhanVienID")
    private NhanVien nhanVien;

    private LocalDateTime NgayLam;

    @ManyToOne @JoinColumn(name = "MaCaLam")
    private CaLam caLam;

    private Integer TongDonXuLy;
    private BigDecimal TongDoanhThu;
}
