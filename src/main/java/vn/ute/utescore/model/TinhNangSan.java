package vn.ute.utescore.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "TinhNang_San")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class TinhNangSan {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne @JoinColumn(name = "maTinhNang", nullable = false)
    private TinhNang tinhNang;

    @ManyToOne @JoinColumn(name = "maSan", nullable = false)
    private SanBong sanBong;
}
