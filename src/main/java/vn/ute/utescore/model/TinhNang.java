package vn.ute.utescore.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "TinhNang")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class TinhNang {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer maTinhNang;

    @Column(length = 20)
    private String tenTinhNang;

    @Column(length = 255)
    private String moTa;
}
