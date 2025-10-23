package vn.ute.utescore.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.*;

@Entity
@Table(name = "SanBong")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class SanBong {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer maSan;

    @Column(nullable = false, length = 20)
    private String tenSan;

    @Column(nullable = false, length = 20)
    private String loaiSan; // 5 người, 7 người, 11 người

    @Column(length = 50)
    private String khuVuc;

    @Column(length = 255)
    private String duongDanGGMap;

    private LocalTime gioMoCua;
    private LocalTime gioDongCua;

    @Column(length = 255)
    private String moTa;

    @Lob
    private byte[] hinhAnh;

    @Column(length = 50)
    private String trangThai; // Hoạt động, Bảo trì, Ngưng sử dụng

    private LocalDateTime ngayTao;
    private LocalDateTime ngayCapNhat;
}
