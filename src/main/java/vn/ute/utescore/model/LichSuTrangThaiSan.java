package vn.ute.utescore.model;


import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "LichSuTrangThaiSan")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class LichSuTrangThaiSan {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer LichSuID;

    @ManyToOne @JoinColumn(name = "SanID")
    private SanBong sanBong;

    @Column(columnDefinition = "NVARCHAR(50)")
    private String TrangThaiCu;

    @Column(columnDefinition = "NVARCHAR(50)")
    private String TrangThaiMoi;

    private LocalDateTime ThoiGianThayDoi;

    @ManyToOne @JoinColumn(name = "NguoiThucHien")
    private NhanVien nguoiThucHien;

    @Column(columnDefinition = "NVARCHAR(255)")
    private String GhiChu;
}
