package vn.ute.utescore.model;


import jakarta.persistence.*;
import lombok.*;

import java.time.LocalTime;
import java.util.List;

@Entity
@Table(name = "CaLam")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class CaLam {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer MaCaLam;

    private LocalTime GioBatDau;
    private LocalTime GioKetThuc;

    @OneToMany(mappedBy = "caLam", cascade = CascadeType.ALL)
    private List<CaLamViec> caLamViecs;
}
