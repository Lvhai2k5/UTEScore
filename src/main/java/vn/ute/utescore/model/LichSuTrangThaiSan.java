package vn.ute.utescore.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.*;

@Entity
@Table(name = "LichSuTrangThaiSan")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class LichSuTrangThaiSan {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer lichSuID;

    @ManyToOne @JoinColumn(name = "sanID", nullable = false)
    private SanBong sanBong;

    @Column(length = 50)
    private String trangThaiCu;

    @Column(length = 50)
    private String trangThaiMoi;

    private LocalDateTime thoiGianThayDoi;

    @ManyToOne @JoinColumn(name = "nguoiThucHien")
    private NhanVien nguoiThucHien;

    @Column(length = 255)
    private String ghiChu;
}
