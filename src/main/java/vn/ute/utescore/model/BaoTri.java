package vn.ute.utescore.model;


import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Table(name = "BaoTri")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class BaoTri {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer BaoTriID;

    @ManyToOne @JoinColumn(name = "MaSan")
    private SanBong sanBong;

    @ManyToOne @JoinColumn(name = "NhanVienID")
    private NhanVien nhanVien;

    private LocalTime ThoiGianBatDau;
    private LocalTime ThoiGianKetThuc;
    private LocalDateTime NgayBaoTri;

    @Column(columnDefinition = "NVARCHAR(255)")
    private String LyDo;

    @Column(columnDefinition = "NVARCHAR(50)")
    private String TrangThai;
}
