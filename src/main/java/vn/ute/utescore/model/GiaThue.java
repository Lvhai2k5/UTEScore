package vn.ute.utescore.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.*;

@Entity
@Table(name = "GiaThue")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class GiaThue {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer maBangGia;

    @Column(length = 20)
    private String loaiSan; // 5 người, 7 người, 11 người

    private LocalTime khungGioBatDau;
    private LocalTime khungGioKetThuc;

    @Column(length = 255)
    private String moTa;

    private Double giaThue;
    private LocalDateTime ngayApDung;

    @Column(length = 50)
    private String trangThai; // Đang áp dụng, Hết hiệu lực
}
