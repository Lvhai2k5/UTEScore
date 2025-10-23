package vn.ute.utescore.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.*;

@Entity
@Table(name = "CaLam")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class CaLam {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer maCaLam;

    private java.time.LocalTime gioBatDau;
    private java.time.LocalTime gioKetThuc;
}
