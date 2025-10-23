package vn.ute.utescore.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.*;

@Entity
@Table(name = "SuCo")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class SuCo {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer suCoID;

    @ManyToOne @JoinColumn(name = "maDon")
    private ThueSan donDat;

    @Column(length = 255)
    private String moTa;

    @Column(length = 50)
    private String loaiSuCo;

    @ManyToOne @JoinColumn(name = "nhanVienID")
    private NhanVien nhanVien;

    private LocalDateTime thoiGianBaoCao;

    @Column(length = 50)
    private String trangThai; // Đang xử lý, Hoàn tất, Đã hủy

    private LocalDateTime thoiGianHoanThanh;
}
