package vn.ute.utescore.model;


import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "SuCo")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class SuCo {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer SuCoID;

    @ManyToOne @JoinColumn(name = "MaDon")
    private ThueSan thueSan;

    @Column(columnDefinition = "NVARCHAR(255)")
    private String MoTa;

    @Column(columnDefinition = "NVARCHAR(50)")
    private String LoaiSuCo;

    @ManyToOne @JoinColumn(name = "NhanVienID")
    private NhanVien nhanVien;

    private LocalDateTime ThoiGianBaoCao;

    @Column(columnDefinition = "NVARCHAR(50)")
    private String TrangThai;

    private LocalDateTime ThoiGianHoanThanh;
}
