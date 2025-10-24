package vn.ute.utescore.model;


import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Table(name = "GiaThue")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class GiaThue {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer MaBangGia;

    @Column(columnDefinition = "NVARCHAR(20)")
    private String LoaiSan;

    private LocalTime KhungGioBatDau;
    private LocalTime KhungGioKetThuc;

    @Column(columnDefinition = "NVARCHAR(255)")
    private String MoTa;

    private BigDecimal GiaThue;
    private LocalDateTime NgayApDung;

    @Column(columnDefinition = "NVARCHAR(50)")
    private String TrangThai;
}
