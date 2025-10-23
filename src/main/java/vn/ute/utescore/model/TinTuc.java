package vn.ute.utescore.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.*;

@Entity
@Table(name = "TinTuc")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class TinTuc {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer tinID;

    @Column(length = 255)
    private String tieuDe;

    @Column(length = 255)
    private String noiDung;

    @Lob
    private byte[] hinhAnh;

    private LocalDateTime ngayDang;

    @Column(length = 20)
    private String trangThai; // Hiển thị, Ẩn
}
