package vn.ute.utescore.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.*;

@Entity
@Table(name = "KhuyenMai")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class KhuyenMai {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer khuyenMaiID;

    @Column(length = 255)
    private String tieuDe;

    @Column(length = 255)
    private String noiDung;

    @Lob
    private byte[] hinhAnh;

    private LocalDateTime ngayBatDau;
    private LocalDateTime ngayKetThuc;

    @Column(length = 50)
    private String trangThai; // Đang diễn ra, Sắp diễn ra, Đã kết thúc
}
