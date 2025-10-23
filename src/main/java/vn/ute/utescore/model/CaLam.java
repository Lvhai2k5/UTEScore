package vn.ute.utescore.model;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "CaLam")
public class CaLam {
@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
private Integer maCaLam;

private LocalTime gioBatDau;
private LocalTime gioKetThuc;
}
