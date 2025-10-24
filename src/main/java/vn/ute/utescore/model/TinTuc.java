package vn.ute.utescore.model;


import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "TinTuc")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class TinTuc {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer TinID;

    @Column(columnDefinition = "NVARCHAR(255)")
    private String TieuDe;

    @Column(columnDefinition = "NVARCHAR(255)")
    private String NoiDung;

    @Lob
    private byte[] HinhAnh;

    private LocalDateTime NgayDang;

    @Column(columnDefinition = "NVARCHAR(50)")
    private String TrangThai;
}
