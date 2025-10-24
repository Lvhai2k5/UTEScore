package vn.ute.utescore.model;


import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "KhuyenMai")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class KhuyenMai {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer KhuyenMaiID;

    @Column(columnDefinition = "NVARCHAR(255)")
    private String TieuDe;

    @Column(columnDefinition = "NVARCHAR(255)")
    private String NoiDung;

    @Lob
    private byte[] HinhAnh;

    private LocalDateTime NgayBatDau;
    private LocalDateTime NgayKetThuc;

    @Column(columnDefinition = "NVARCHAR(50)")
    private String TrangThai;
}
