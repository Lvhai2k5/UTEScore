package vn.ute.utescore.model;


import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "TinhNang")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class TinhNang {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer MaTinhNang;

    @Column(columnDefinition = "NVARCHAR(20)")
    private String TenTinhNang;

    @Column(columnDefinition = "NVARCHAR(255)")
    private String MoTa;

    @OneToMany(mappedBy = "tinhNang", cascade = CascadeType.ALL)
    private List<TinhNangSan> tinhNangSans;
}
